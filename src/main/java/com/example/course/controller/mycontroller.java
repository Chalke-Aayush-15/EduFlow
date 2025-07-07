package com.example.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.course.service.courseservice;

@Controller
//@RequestMapping("")
public class mycontroller {
	
	@Autowired
	private courseservice cs;

	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("Course", cs.getcourse());
		return "Home";
	}
	
	@GetMapping("/aboutus")
	public String about() {
		return "aboutus";
	}
	
	@GetMapping("/contactus")
	public String contact() {
		return "contactus";
	}
	
}
