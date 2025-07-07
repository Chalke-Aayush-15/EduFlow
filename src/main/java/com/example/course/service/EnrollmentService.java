// Enrollment Service
package com.example.course.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.course.entity.Enrollment;
import com.example.course.entity.course;
import com.example.course.entity.userentity;
import com.example.course.repository.EnrollmentRepository;

@Service
@Transactional
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    @Autowired
    private courseservice courseService; // Assuming you have this
    
    @Autowired
    private UserService userService; // Assuming you have this
    
    // Enroll a user in a course
    public Enrollment enrollUser(int userId, int courseId) {
        // Check if user is already enrolled
        Optional<Enrollment> existingEnrollment = enrollmentRepository.findByUserIdAndCourse_Cid(userId, courseId);
        if (existingEnrollment.isPresent()) {
            throw new RuntimeException("User is already enrolled in this course");
        }
        
        // Get user and course entities
        userentity user = userService.findById(userId); // You need to implement this
        course course = courseService.findById(courseId); // You need to implement this
        
        if (user == null || course == null) {
            throw new RuntimeException("User or Course not found");
        }
        
        // Create new enrollment
        Enrollment enrollment = new Enrollment(user, course);
        enrollment.setAmountPaid(course.getCprice());
        enrollment.setPaymentStatus("PAID"); // Set based on your payment logic
        
        return enrollmentRepository.save(enrollment);
    }
    
    // Get all enrollments for a user
    public List<Enrollment> getUserEnrollments(int userId) {
        return enrollmentRepository.findEnrollmentsWithDetailsByUserId(userId);
    }
    
    
    public List<Enrollment> getEnrollmentsByUserId(int userId) {
        return enrollmentRepository.findByUserId(userId);
    }
    
    
    // Get all enrollments for admin dashboard
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAllEnrollmentsWithDetails();
    }
    
    // Get enrollments for a specific course
    public List<Enrollment> getCourseEnrollments(int courseId) {
        return enrollmentRepository.findByCourse_Cid(courseId);
    }
    
    // Cancel enrollment
    public void cancelEnrollment(int enrollmentId) {
        Optional<Enrollment> enrollment = enrollmentRepository.findById(enrollmentId);
        if (enrollment.isPresent()) {
            enrollment.get().setEnrollmentStatus("CANCELLED");
            enrollmentRepository.save(enrollment.get());
        } else {
            throw new RuntimeException("Enrollment not found");
        }
    }
    
    // Complete enrollment
    public void completeEnrollment(int enrollmentId) {
        Optional<Enrollment> enrollment = enrollmentRepository.findById(enrollmentId);
        if (enrollment.isPresent()) {
            enrollment.get().setEnrollmentStatus("COMPLETED");
            enrollment.get().setCompletedAt(Instant.now());
            enrollmentRepository.save(enrollment.get());
        } else {
            throw new RuntimeException("Enrollment not found");
        }
    }
    
    // Check if user is enrolled in course
    public boolean isUserEnrolled(int userId, int courseId) {
        return enrollmentRepository.findByUserIdAndCourse_Cid(userId, courseId).isPresent();
    }
    
    // Get enrollment count for a course
    public Long getCourseEnrollmentCount(int courseId) {
        return enrollmentRepository.countActiveByCourseId(courseId);
    }
    
    // Get recent enrollments for dashboard
    public List<Enrollment> getRecentEnrollments() {
        return enrollmentRepository.findRecentEnrollments();
    }
    
    // Get enrollment by ID
    public Enrollment getEnrollmentById(int enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
    }
    
    // Update payment status
    public void updatePaymentStatus(int enrollmentId, String paymentStatus) {
        Optional<Enrollment> enrollment = enrollmentRepository.findById(enrollmentId);
        if (enrollment.isPresent()) {
            enrollment.get().setPaymentStatus(paymentStatus);
            enrollmentRepository.save(enrollment.get());
        } else {
            throw new RuntimeException("Enrollment not found");
        }
    }
    
    // update the enrollment after pay
    public Enrollment enrollUserWithPayment(int userId, int courseId, String paymentId, double amountPaid) {
        // Check if user is already enrolled
        Optional<Enrollment> existingEnrollment = enrollmentRepository.findByUserIdAndCourse_Cid(userId, courseId);
        if (existingEnrollment.isPresent()) {
            throw new RuntimeException("User is already enrolled in this course");
        }
        
        // Get user and course entities
        userentity user = userService.findById(userId);
        course course = courseService.findById(courseId);
        
        if (user == null || course == null) {
            throw new RuntimeException("User or Course not found");
        }
        
        // Create new enrollment with payment details
        Enrollment enrollment = new Enrollment(user, course);
        enrollment.setAmountPaid(amountPaid);
        enrollment.setPaymentStatus("PAID");
        enrollment.setEnrollmentStatus("ACTIVE");
        enrollment.setPaymentId(paymentId); // Store Razorpay payment ID
        
        return enrollmentRepository.save(enrollment);
    }
}