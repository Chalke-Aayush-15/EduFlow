package com.example.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.course.entity.userentity;
import com.example.course.repository.userrepo;
import com.example.course.service.EmailService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/valid")
public class AuthController {
    
    @Autowired 
    private userrepo ur;
    
    @Autowired 
    private EmailService emailService;
    
    @GetMapping("/register")
    public String register(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new userentity());
        }
        return "register";
    }
    
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new userentity());
        return "login";
    }
    
    @PostMapping("/register")
    public String add(@Valid @ModelAttribute("user") userentity user, 
                     BindingResult bindingResult, 
                     Model model, 
                     RedirectAttributes redirectAttributes) {
        
        // Log the received user data for debugging
        System.out.println("Received user data:");
        System.out.println("First Name: " + user.getFname());
        System.out.println("Last Name: " + user.getLname());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Password: " + (user.getPassword() != null ? "[HIDDEN]" : "null"));
        
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            System.out.println("Validation errors found:");
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println("Error: " + error.getDefaultMessage());
            });
            model.addAttribute("error", "Please correct the validation errors");
            return "register";
        }
        
        try {
            // Set default role
            user.setRole("User");
            
            // Check if user already exists
            if (ur.existsByEmail(user.getEmail())) {
                model.addAttribute("error", "Email already exists");
                return "register";
            }
            
            // Save user
            userentity savedUser = ur.save(user);
            System.out.println("User saved successfully with ID: " + savedUser.getId());
            
            // Send welcome email
            try {
                emailService.sendEmail(
                    user.getEmail(),
                    "Welcome to EduFlow!",
                    "Welcome to EduFlow - Your Course Management Platform! " +
                    "Your account has been created successfully. " +
                    "Continue your learning journey with us. Thank you!"
                );
                System.out.println("Welcome email sent successfully");
            } catch (Exception emailEx) {
                System.err.println("Failed to send welcome email: " + emailEx.getMessage());
                emailEx.printStackTrace();
                // Continue with registration even if email fails
            }
            
            redirectAttributes.addFlashAttribute("success", "Account created successfully! Please login.");
            return "redirect:/valid/login";
            
        } catch (DataIntegrityViolationException e) {
            System.err.println("Database constraint violation: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Email already exists or invalid data");
            return "register";
        } catch (Exception e) {
            System.err.println("Unexpected error during registration: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An unexpected error occurred while creating your account");
            return "register";
        }
    }
    
    @PostMapping("/login")
    public String loggedin(@RequestParam String email, 
                          @RequestParam String password, 
                          Model model, 
                          HttpSession session) {
        
        System.out.println("Login attempt - Email: " + email);
        
        try {
            userentity user = ur.findByEmailAndPassword(email, password);
            
            if (user != null) {
                session.setAttribute("loggeduser", user);
                System.out.println("User logged in successfully: " + user.getEmail());
                
                if ("Admin".equalsIgnoreCase(user.getRole())) {
                    return "redirect:/Admin/";
                } else {
                    return "redirect:/user/index";
                }
            } else {
                System.out.println("Invalid credentials for email: " + email);
                model.addAttribute("error", "Invalid email or password");
                return "login";
            }
        } catch (Exception e) {
            System.err.println("Error during login: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred during login");
            return "login";
        }
    }
}