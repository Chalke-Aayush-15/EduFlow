package com.example.course.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.course.entity.userentity;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class admincontroller {
	
	@GetMapping("/admin")
	public String adminhome(HttpSession session, Model model) {
		userentity user = (userentity) session.getAttribute("loggedUser");
		
		if (user != null && "Admin".equalsIgnoreCase(user.getRole()))
		{
			return "adminpanel";
		}
		return "redirect:/login";
	}
}
 