// New Enrollment Entity (Join Table)
package com.example.course.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "enrollments")
@EntityListeners(AuditingEntityListener.class)
public class Enrollment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eid;
    
    // ManyToOne: Many enrollments belong to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private userentity user;
    
    // ManyToOne: Many enrollments belong to one course
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private course course;
    private String paymentId;
    private String enrollmentStatus = "ACTIVE"; // ACTIVE, COMPLETED, CANCELLED
    private String paymentStatus = "PENDING"; // PENDING, PAID, FAILED
    private double amountPaid = 0.0;
    
    @CreatedDate
    private Instant enrolledAt;
    
    private Instant completedAt;
    
    // Constructors
    public Enrollment() {}
    
    public Enrollment(userentity user, course course) {
        this.user = user;
        this.course = course;
    }
    
    // Getters and Setters
    public int getEid() {
        return eid;
    }
    public void setEid(int eid) {
        this.eid = eid;
    }
    
    public userentity getUser() {
        return user;
    }
    public void setUser(userentity user) {
        this.user = user;
    }
    
    public course getCourse() {
        return course;
    }
    public void setCourse(course course) {
        this.course = course;
    }
    
    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }
    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public double getAmountPaid() {
        return amountPaid;
    }
    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }
    
    public Instant getEnrolledAt() {
        return enrolledAt;
    }
    public void setEnrolledAt(Instant enrolledAt) {
        this.enrolledAt = enrolledAt;
    }
    
    public Instant getCompletedAt() {
        return completedAt;
    }
    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
    
 // Update the toString method to include paymentId
    @Override
    public String toString() {
        return "Enrollment [eid=" + eid + ", userId=" + (user != null ? user.getId() : null) + 
               ", courseId=" + (course != null ? course.getCid() : null) + 
               ", enrollmentStatus=" + enrollmentStatus + ", paymentStatus=" + paymentStatus + 
               ", amountPaid=" + amountPaid + ", paymentId=" + paymentId + 
               ", enrolledAt=" + enrolledAt + "]";
    }
}