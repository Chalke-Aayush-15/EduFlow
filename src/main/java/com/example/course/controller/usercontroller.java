package com.example.course.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
		
		if (user != null && "USER".equalsIgnoreCase(user.getRole()))
		{
			model.addAttribute("user", user); // ✅ THIS IS MISSING
			model.addAttribute("Course", cs.getcourse());
			model.addAttribute("username",user.getFname() +" "+ user.getLname());
			model.addAttribute("init",user.getInitials());
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
	        // Get logged-in user
	        userentity user = (userentity) session.getAttribute("loggeduser");
	        if (user == null) {
	            response.put("success", false);
	            response.put("message", "User not logged in");
	            return ResponseEntity.badRequest().body(response);
	        }
	        
	        // Extract request data
	        Integer courseId = (Integer) request.get("courseId");
	        String paymentId = (String) request.get("paymentId");
	        Double amountPaid = (Double) request.get("amountPaid");
	        
	        if (courseId == null || paymentId == null || amountPaid == null) {
	            response.put("success", false);
	            response.put("message", "Missing required data");
	            return ResponseEntity.badRequest().body(response);
	        }
	        
	        // Check if user is already enrolled
	        boolean alreadyEnrolled = enrollmentService.isUserEnrolled(user.getId(), courseId);
	        if (alreadyEnrolled) {
	            response.put("success", false);
	            response.put("message", "User is already enrolled in this course");
	            return ResponseEntity.badRequest().body(response);
	        }
	        
	        // Create enrollment
	        Enrollment enrollment = enrollmentService.enrollUserWithPayment(
	            user.getId(), courseId, paymentId, amountPaid);
	        
	        response.put("success", true);
	        response.put("message", "Enrollment successful");
	        response.put("enrollmentId", enrollment.getEid());
	        
	        return ResponseEntity.ok(response);
	        
	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("message", "Enrollment failed: " + e.getMessage());
	        return ResponseEntity.badRequest().body(response);
	    }
	}
	
	@GetMapping("/usercalender")
	public String usercalender(Model model, HttpSession session) 
	{
		userentity user = (userentity) session.getAttribute("loggeduser"); 
		
		model.addAttribute("username",user.getFname() +" "+ user.getLname());
		model.addAttribute("init",user.getInitials());
		return "usercoursecalender";
	}
	
	@GetMapping("/mycourse")
	public String mycourse(Model model, HttpSession session) {
		userentity user = (userentity) session.getAttribute("loggeduser"); 
		
		String email = user.getEmail(); // Logged-in user's email
        userentity user1 = us.getUserByEmail(email);
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByUserId(user1.getId());
        model.addAttribute("enrollments", enrollments);
		model.addAttribute("username",user.getFname() +" "+ user.getLname());
		model.addAttribute("init",user.getInitials());
		return "usercourse";
	 
	}
	
	@GetMapping("/setting")
	public String setting(Model model, HttpSession session)
	{
		userentity user = (userentity) session.getAttribute("loggeduser"); 
		
		model.addAttribute("username",user.getFname() +" "+ user.getLname());
		model.addAttribute("init",user.getInitials());
		model.addAttribute("use",user);
		return "settinguser";
	}
	
	@PostMapping("/update")
	public String updateuser(@ModelAttribute userentity use)
	{
		us.updateuser(use);
		return "redirect:/user/setting";
	}
	
	@GetMapping("/logout")
	public String userlogout(HttpSession session)
	{
		session.invalidate();
		return "redirect:/valid/login";
	}
	
	
//	
//	
}
