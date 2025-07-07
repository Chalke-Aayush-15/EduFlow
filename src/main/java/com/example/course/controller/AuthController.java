 package com.example.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.course.entity.userentity;
import com.example.course.repository.userrepo;
import com.example.course.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/valid")
public class AuthController {
	
	@Autowired 
	private userrepo ur;
	@Autowired 
	private EmailService emailService;
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("user", new userentity());
		return "register";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("user", new userentity());
		return "login";
	}
	
	@PostMapping("/register")
	public String add(@ModelAttribute userentity user, Model model) {
		try {
			user.setRole("User");
			ur.save(user);

			// ✅ Send welcome email here
			try {
			emailService.sendEmail(
			user.getEmail(),
			"Account created successfully!",
			"Welcome to EduFlow the Course mangement web. Continue your learning with us... Thank you!"
			);
			} catch (Exception emailEx) {
			emailEx.printStackTrace();
			model.addAttribute("error",  "Account created, but failed to send confirmation email.");
			return "register"; // Optional: redirect anyway or show success
			}

			return "redirect:/valid/login?success=true";
			} catch (DataIntegrityViolationException e) {
			model.addAttribute("error", "Email or username already exists");
			return "register";
			} catch (Exception e) {
			model.addAttribute("error", "An error occurred while creating your account");
			return "register";
			}
	}
	
	@PostMapping("/login")
	public String loggedin(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
		System.out.println("Email: " + email + "\nPassword: " + password);
		
		userentity ue = ur.findByEmailAndPassword(email, password);
		if (ue!=null) {
			session.setAttribute("loggeduser", ue);
			
			if ("Admin".equalsIgnoreCase(ue.getRole())) {
				return "redirect:/Admin/";
			}
			
			
			else {
				return "redirect:/user/index";
			}
		}
		else {
			model.addAttribute("error","Invalid Credentials");
			return "login";
		}
	}
}
