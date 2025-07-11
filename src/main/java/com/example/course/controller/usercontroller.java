package com.example.course.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.course.entity.Enrollment;
import com.example.course.entity.course;
import com.example.course.entity.userentity;
import com.example.course.repository.EnrollmentRepository;
import com.example.course.repository.courserepo;
import com.example.course.repository.userrepo;
import com.example.course.service.EnrollmentService;
import com.example.course.service.UserService;
import com.example.course.service.courseservice;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class usercontroller {
	
	@Autowired
	private EnrollmentService enrollmentService;
	@Autowired
	private courseservice cs;
	@Autowired
	private UserService us;

	@GetMapping("/index")
	public String userhome(Model model, HttpSession session) {
	    userentity user = (userentity) session.getAttribute("loggeduser"); 
	    
	    if (user != null && "USER".equalsIgnoreCase(user.getRole())) {
	        List<course> courses = cs.getcourse();
	        Map<Integer, Boolean> enrollmentStatus = new HashMap<>();
	        
	        // Check enrollment status for each course
	        for (course course : courses) {
	            boolean isEnrolled = enrollmentService.isUserEnrolled(user.getId(), course.getCid());
	            enrollmentStatus.put(course.getCid(), isEnrolled);
	        }
	        
	        model.addAttribute("user", user);
	        model.addAttribute("Course", courses);
	        model.addAttribute("enrollmentStatus", enrollmentStatus);
	        model.addAttribute("username", user.getFname() + " " + user.getLname());
	        model.addAttribute("init", user.getInitials());
	        return "userindex";
	    }
	    return "redirect:/valid/login";
	}

	@PostMapping("/enroll-after-payment")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> enrollAfterPayment(
	        @RequestBody Map<String, Object> request,
	        HttpSession session) {
	    
	    Map<String, Object> response = new HashMap<>();
	    
	    try {
	        System.out.println("Enrollment request received: " + request);
	        
	        // Get logged-in user from session
	        userentity user = (userentity) session.getAttribute("loggeduser");
	        
	        if (user == null) {
	            System.err.println("User not found in session");
	            response.put("success", false);
	            response.put("message", "User not logged in");
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	        }
	        
	        System.out.println("User found: " + user.getEmail() + " (ID: " + user.getId() + ")");

	        // Extract request data - Fix the type casting
	        Object courseIdObj = request.get("courseId");
	        Object paymentIdObj = request.get("paymentId");
	        Object amountPaidObj = request.get("amountPaid");
	        
	        if (courseIdObj == null || paymentIdObj == null || amountPaidObj == null) {
	            System.err.println("Missing required data in request");
	            response.put("success", false);
	            response.put("message", "Missing required data");
	            return ResponseEntity.badRequest().body(response);
	        }
	        
	        // Convert to proper types
	        int courseId;
	        if (courseIdObj instanceof String) {
	            courseId = Integer.parseInt((String) courseIdObj);
	        } else {
	            courseId = (Integer) courseIdObj;
	        }
	        
	        String paymentId = paymentIdObj.toString();
	        
	        double amountPaid;
	        if (amountPaidObj instanceof String) {
	            amountPaid = Double.parseDouble((String) amountPaidObj);
	        } else if (amountPaidObj instanceof Integer) {
	            amountPaid = ((Integer) amountPaidObj).doubleValue();
	        } else {
	            amountPaid = (Double) amountPaidObj;
	        }
	        
	        System.out.println("Processing enrollment - CourseId: " + courseId + ", PaymentId: " + paymentId + ", Amount: " + amountPaid);
	        
	        // Check if user is already enrolled
	        boolean alreadyEnrolled = enrollmentService.isUserEnrolled(user.getId(), courseId);
	        if (alreadyEnrolled) {
	            System.err.println("User already enrolled in course");
	            response.put("success", false);
	            response.put("message", "User is already enrolled in this course");
	            return ResponseEntity.badRequest().body(response);
	        }
	        
	        // Create enrollment
	        Enrollment enrollment = enrollmentService.enrollUserWithPayment(
	            user.getId(), courseId, paymentId, amountPaid);
	        
	        System.out.println("Enrollment created successfully: " + enrollment);
	        
	        response.put("success", true);
	        response.put("message", "Enrollment successful");
	        response.put("enrollmentId", enrollment.getEid());
	        
	        return ResponseEntity.ok(response);
	        
	    } catch (NumberFormatException e) {
	        System.err.println("Number format error: " + e.getMessage());
	        response.put("success", false);
	        response.put("message", "Invalid number format in request data");
	        return ResponseEntity.badRequest().body(response);
	    } catch (Exception e) {
	        System.err.println("Enrollment error: " + e.getMessage());
	        e.printStackTrace();
	        response.put("success", false);
	        response.put("message", "Enrollment failed: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

	@GetMapping("/usercalender")
	public String usercalender(Model model, HttpSession session) {
		userentity user = (userentity) session.getAttribute("loggeduser"); 
		
		if (user == null) {
			return "redirect:/valid/login";
		}
		
		model.addAttribute("username", user.getFname() + " " + user.getLname());
		model.addAttribute("init", user.getInitials());
		return "usercoursecalender";
	}
	
	@GetMapping("/mycourse")
	public String mycourse(Model model, HttpSession session) {
		userentity user = (userentity) session.getAttribute("loggeduser"); 
		
		if (user == null) {
			return "redirect:/valid/login";
		}
		
		try {
			// Get enrollments directly using the user ID from session
			List<Enrollment> enrollments = enrollmentService.getEnrollmentsByUserId(user.getId());
			System.out.println("Found " + enrollments.size() + " enrollments for user " + user.getId());
			
			model.addAttribute("enrollments", enrollments);
			model.addAttribute("username", user.getFname() + " " + user.getLname());
			model.addAttribute("init", user.getInitials());
			
		} catch (Exception e) {
			System.err.println("Error loading user courses: " + e.getMessage());
			e.printStackTrace();
		}
		
		return "usercourse";
	}
	
	@GetMapping("/setting")
	public String setting(Model model, HttpSession session) {
		userentity user = (userentity) session.getAttribute("loggeduser"); 
		
		if (user == null) {
			return "redirect:/valid/login";
		}
		
		model.addAttribute("username", user.getFname() + " " + user.getLname());
		model.addAttribute("init", user.getInitials());
		model.addAttribute("use", user);
		return "settinguser";
	}
	
	@PostMapping("/update")
	public String updateuser(@ModelAttribute userentity use) {
		us.updateuser(use);
		return "redirect:/user/setting";
	}
	
	@GetMapping("/logout")
	public String userlogout(HttpSession session) {
		session.invalidate();
		return "redirect:/valid/login";
	}
}