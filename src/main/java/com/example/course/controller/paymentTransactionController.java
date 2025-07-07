package com.example.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.course.entity.orderTransactionDetails;
import com.example.course.service.orderServices;

@RestController
public class paymentTransactionController 
{
	@Autowired
	private orderServices orderservices;
	@CrossOrigin(origins = "*")
	@GetMapping("/getTransaction/{amount}")
	public orderTransactionDetails getTransaction(@PathVariable(name="amount") double amount) {
        orderTransactionDetails transactionDetails = orderservices.orderCreateTransaction(amount);
        if (transactionDetails != null) {
            return transactionDetails;
        } else {
            // Handle error case
            return null;  // Return an error response, or you could throw a custom exception
        }
    }
	
	

}
