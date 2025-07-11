package com.example.course.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.course.service.EmailService;

@RestController
@RequestMapping("/mail")
public class EmailController {

    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private EmailService emailService;

    @PostMapping("/sendEmail")
    public ResponseEntity<String> sendEmail(@RequestBody Map<String, Object> request) {
        try {
            String email = (String) request.get("email");
            logger.info("Sending email to: {}", email);
            
            emailService.sendEmail(email, "Test Subject", "Test Message");
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            logger.error("Failed to send email: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email: " + e.getMessage());
        }
    }

    @PostMapping("/sendEnrollmentEmail")
    public ResponseEntity<String> sendEnrollmentEmail(@RequestBody Map<String, Object> request) {
        try {
            String email = (String) request.get("to");
            String userName = (String) request.get("userName");
            String courseName = (String) request.get("courseName");
            String paymentId = (String) request.get("paymentId");
            Double amountPaid = Double.valueOf(request.get("amountPaid").toString());
            String enrollmentDate = (String) request.get("enrollmentDate");
            
            logger.info("Sending enrollment email to: {} for course: {}", email, courseName);
            
            // Validate required fields
            if (email == null || email.trim().isEmpty()) {
                logger.error("Email address is null or empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Email address is required");
            }
            
            if (userName == null || userName.trim().isEmpty()) {
                userName = "Student";
            }
            
            String subject = "🎉 Course Enrollment Confirmation - " + courseName;
            String message = buildEnrollmentEmailMessage(userName, courseName, paymentId, amountPaid, enrollmentDate);
            
            emailService.sendEmail(email, subject, message);
            logger.info("Enrollment email sent successfully to: {}", email);
            
            return ResponseEntity.ok("Enrollment email sent successfully");
        } catch (Exception e) {
            logger.error("Failed to send enrollment email: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send enrollment email: " + e.getMessage());
        }
    }
    
    private String buildEnrollmentEmailMessage(String userName, String courseName, String paymentId, 
                                             Double amountPaid, String enrollmentDate) {
        return "Dear " + userName + ",\n\n" +
               "🎉 Congratulations! Your payment has been processed successfully and you are now enrolled in the course.\n\n" +
               "📚 Course Details:\n" +
               "Course Name: " + courseName + "\n" +
               "Payment ID: " + paymentId + "\n" +
               "Amount Paid: ₹" + amountPaid + "\n" +
               "Enrollment Date: " + enrollmentDate + "\n\n" +
               "✅ What's Next?\n" +
               "• You can now access your course materials in the dashboard\n" +
               "• Check your 'My Courses' section to start learning\n" +
               "• Join our community discussions\n\n" +
               "📧 Need Help?\n" +
               "If you have any questions or need assistance, please don't hesitate to contact our support team.\n\n" +
               "Thank you for choosing EduFlow!\n" +
               "Happy Learning! 🚀\n\n" +
               "Best regards,\n" +
               "The EduFlow Team";
    }
}