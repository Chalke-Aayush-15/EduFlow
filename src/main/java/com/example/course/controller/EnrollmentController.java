// Controller for Enrollment
package com.example.course.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.course.entity.Enrollment;
import com.example.course.service.EnrollmentService;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;
    
    // Enroll user in course
    @PostMapping("/enroll")
    public ResponseEntity<?> enrollUser(@RequestParam int userId, @RequestParam int courseId) {
        try {
            Enrollment enrollment = enrollmentService.enrollUser(userId, courseId);
            return ResponseEntity.ok(enrollment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Get user's enrollments
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Enrollment>> getUserEnrollments(@PathVariable int userId) {
        List<Enrollment> enrollments = enrollmentService.getUserEnrollments(userId);
        
        return ResponseEntity.ok(enrollments);
    }
    
    // Get all enrollments (for admin)
    @GetMapping("/admin/all")
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }
    
    // Get course enrollments
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Enrollment>> getCourseEnrollments(@PathVariable int courseId) {
        List<Enrollment> enrollments = enrollmentService.getCourseEnrollments(courseId);
        return ResponseEntity.ok(enrollments);
    }
    
    // Cancel enrollment
    @PutMapping("/cancel/{enrollmentId}")
    public ResponseEntity<?> cancelEnrollment(@PathVariable int enrollmentId) {
        try {
            enrollmentService.cancelEnrollment(enrollmentId);
            return ResponseEntity.ok("Enrollment cancelled successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Complete enrollment
    @PutMapping("/complete/{enrollmentId}")
    public ResponseEntity<?> completeEnrollment(@PathVariable int enrollmentId) {
        try {
            enrollmentService.completeEnrollment(enrollmentId);
            return ResponseEntity.ok("Enrollment completed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Check if user is enrolled
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkEnrollment(@RequestParam int userId, @RequestParam int courseId) {
        boolean isEnrolled = enrollmentService.isUserEnrolled(userId, courseId);
        return ResponseEntity.ok(isEnrolled);
    }
    
    // Get recent enrollments
    @GetMapping("/recent")
    public ResponseEntity<List<Enrollment>> getRecentEnrollments() {
        List<Enrollment> enrollments = enrollmentService.getRecentEnrollments();
        return ResponseEntity.ok(enrollments);
    }
}