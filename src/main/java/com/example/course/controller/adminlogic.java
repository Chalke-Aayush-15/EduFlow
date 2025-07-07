package com.example.course.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.course.entity.Enrollment;
import com.example.course.entity.course;
import com.example.course.entity.userentity;
import com.example.course.repository.EnrollmentRepository;
import com.example.course.repository.courserepo;
import com.example.course.repository.userrepo;
import com.example.course.service.courseservice;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Admin")
public class adminlogic {
	
	@Autowired courseservice cs;
	
	@Autowired
	private courserepo courseRepo;
	
	@Autowired
	private userrepo userRepo;
	
	@Autowired
	private EnrollmentRepository enrollmentRepo;
	
	@GetMapping("/")
	public String test(Model model) {
		model.addAttribute("TotalCourse", cs.gettotalcourse());
		model.addAttribute("Totalstudent", cs.gettotalstudent());
		model.addAttribute("TotalInstructor", cs.gettotalinstructor());
		return "adminpanel";
	}
	
	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("Course", new course());
		return "addcourse";
	}
	
	@PostMapping("/submit-item")
	public String submit( @ModelAttribute course course) {
		cs.submitcourse(course);
		return "redirect:/Admin/";
	} 
	
	@GetMapping("/delete/{id}")
	public String deletecourse(@PathVariable("id") int id) {
		cs.deletebyid(id);
		return "redirect:/Admin/";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") int id, Model model) {
		course course = cs.getcoursebyid(id);
		model.addAttribute("course",course);
		return "editcourse";
	}
	
	@PostMapping("/update")
	public String updatecourse(@ModelAttribute course course) {
		cs.updatecourse(course);
		return "redirect:/Admin/";
	}
	
	@GetMapping("/course")
	public String coursedetail(Model model) {
		model.addAttribute("TotalCourse", cs.gettotalcourse());
		model.addAttribute("Course",cs.getcourse());
		model.addAttribute("ActiveCourse",cs.gettotalactivecourse());
		return "courseadmin";
	}
	
	@GetMapping("/useradmin")
	public String useradmin(Model model) {
		model.addAttribute("User",cs.getusertable());
		model.addAttribute("TotalAdmin",cs.gettotaladmin());
		model.addAttribute("Totalstudent", cs.gettotalstudent());
		model.addAttribute("TotalInstructor", cs.gettotalinstructor());
		model.addAttribute("TotalUser", cs.gettotaluser());
		return "userAdmin";
	}
	
	@GetMapping("/instructor")
	public String instructorAdmin(Model model) {
		model.addAttribute("TotalInstructor", cs.gettotalinstructor());
		return "instructorAdmin";
	}
	
	@GetMapping("/enrollment")
	public String enroll(Model model){
		List<Enrollment> enroll = enrollmentRepo.findAll();
		model.addAttribute("enroll",enroll);
		return "enrollmentAdmin";
	}
	
	@GetMapping("/setting")
	public String set() {
		return "settingAdmin";
	}
	
	@GetMapping("/logout")
	public String logoutadmin(HttpSession session) {
		session.invalidate();
		return "redirect:/valid/login";
	}
	
	 @GetMapping("/addenroll")
	    public String showEnrollmentForm(Model model) {
	        model.addAttribute("enrollment", new Enrollment());
	        model.addAttribute("users", userRepo.findAll());
	        model.addAttribute("courses", courseRepo.findAll());
	        return "addenrollment";
	    }

	    @PostMapping("/submit_enrollment")
	    public String submitEnrollment(
	        @RequestParam("user_id") int userId,
	        @RequestParam("course_id") int courseId,
	        @RequestParam("enrolment_status") String enrollmentStatus,
	        @RequestParam("payment_status") String paymentStatus,
	        @RequestParam("amount_paid") double amountPaid,
	        @RequestParam("enrolledAt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime enrolledAt,
	        @RequestParam(value = "completedAt", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime completedAt,
	        Model model
	    ) {
	        userentity user = userRepo.findById(userId).orElse(null);
	        course course = courseRepo.findById(courseId).orElse(null);

	        System.out.println("User: "+user);
	        System.out.println("Course: "+course);
	        
	        if (user == null || course == null) {
	            model.addAttribute("error", "Invalid User ID or Course ID");
	            return "addenrollment";
	        }

	        Enrollment enrollment = new Enrollment();
	        enrollment.setUser(user);
	        enrollment.setCourse(course);
	        enrollment.setEnrollmentStatus(enrollmentStatus);
	        enrollment.setPaymentStatus(paymentStatus);
	        enrollment.setAmountPaid(amountPaid);
	        enrollment.setEnrolledAt(enrolledAt.atZone(ZoneId.systemDefault()).toInstant());

	        if (completedAt != null) {
	            enrollment.setCompletedAt(completedAt.atZone(ZoneId.systemDefault()).toInstant());
	        }

	        enrollmentRepo.save(enrollment);
	        
	        System.out.println("Data: "+enrollment);

	        return "redirect:/Admin/enrollment";
	    }
	
	@GetMapping("/users")
	public String getUsers(@RequestParam(value = "role", required = false) String role, Model model) {
	    List<userentity> users;
	    
	    if (role == null || role.equalsIgnoreCase("ALL")) {
	        users = cs.getAllUsers();
	    } else {
	        users = cs.getUsersByRole(role.toUpperCase());
	    }

		model.addAttribute("User",cs.getusertable());
		model.addAttribute("TotalAdmin",cs.gettotaladmin());
		model.addAttribute("Totalstudent", cs.gettotalstudent());
		model.addAttribute("TotalInstructor", cs.gettotalinstructor());
		model.addAttribute("TotalUser", cs.gettotaluser());
	    model.addAttribute("User", users);
	    model.addAttribute("activeRole", role == null ? "ALL" : role.toUpperCase());

	    return "userAdmin"; // your HTML page name
	}

}
