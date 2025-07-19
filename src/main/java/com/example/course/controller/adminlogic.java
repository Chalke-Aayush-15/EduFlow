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
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Add these imports to your CourseService class
import java.io.IOException;
import java.io.PrintWriter;
// OR for Spring Boot 3.x+
import jakarta.servlet.http.HttpServletResponse;

// If you're using Spring Boot 3.x+, make sure you have:
import jakarta.servlet.http.HttpServletResponse;

// If you're using Spring Boot 2.x, use:

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
		model.addAttribute("roll",enrollmentRepo.count());
		model.addAttribute("Totalstudent", cs.gettotalstudent());
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
	
	// Add this method to your AdminController class

	@GetMapping("/export-csv")
	public void exportCoursesToCSV(HttpServletResponse response) throws IOException {
	    // Set response headers
	    response.setContentType("text/csv");
	    response.setHeader("Content-Disposition", "attachment; filename=\"courses.csv\"");
	    
	    // Get all courses
	    List<course> courses = cs.getcourse();
	    
	    // Create CSV writer
	    PrintWriter writer = response.getWriter();
	    
	    // Write CSV header
	    writer.println("Course ID,Course Name,Instructor,Enrollment Count,Created Date,Status,Description,Duration,Price");
	    
	    // Write course data
	    for (course course : courses) {
	        StringBuilder line = new StringBuilder();
	        line.append(escapeCSV(String.valueOf(course.getCid()))).append(",");
	        line.append(escapeCSV(course.getCname())).append(",");
	        line.append(escapeCSV(course.getCtutor())).append(",");
	        line.append(escapeCSV(String.valueOf(course.getCenrollment()))).append(",");
	        line.append(escapeCSV(course.getCreatedAt() != null ? course.getCreatedAt().toString() : "")).append(",");
	        line.append(escapeCSV(course.getStatus())).append(",");
	        line.append(escapeCSV(course.getCdesc() != null ? course.getCdesc() : "")).append(",");
	        line.append(escapeCSV(course.getCduration() != null ? course.getCduration() : "")).append(",");
	        line.append(escapeCSV(course.getCprice() != null ? course.getCprice().toString() : ""));
	        
	        writer.println(line.toString());
	    }
	    
	    writer.flush();
	    writer.close();
	}

	// Helper method to escape CSV values
	private String escapeCSV(String value) {
	    if (value == null) {
	        return "";
	    }
	    
	    // If the value contains comma, quote, or newline, wrap it in quotes
	    if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
	        // Replace any quotes with double quotes
	        value = value.replace("\"", "\"\"");
	        return "\"" + value + "\"";
	    }
	    
	    return value;
	}

}
