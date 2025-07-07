// Enrollment Repository
package com.example.course.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.course.entity.Enrollment;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    
    // Find all enrollments for a specific user
    List<Enrollment> findByUserId(int userId);
    
    
    
    // Find all enrollments for a specific course
    List<Enrollment> findByCourse_Cid(int courseId);
    
    // Check if user is already enrolled in a course
    Optional<Enrollment> findByUserIdAndCourse_Cid(int userId, int courseId);
    
    // Find enrollments by status
    List<Enrollment> findByEnrollmentStatus(String status);
    
    // Find enrollments by payment status
    List<Enrollment> findByPaymentStatus(String paymentStatus);
    
    // Custom query to get enrollment details with user and course info
    @Query("SELECT e FROM Enrollment e JOIN FETCH e.user JOIN FETCH e.course WHERE e.user.id = :userId")
    List<Enrollment> findEnrollmentsWithDetailsByUserId(@Param("userId") int userId);
    
    // Custom query to get all enrollments with user and course details for admin
    @Query("SELECT e FROM Enrollment e JOIN FETCH e.user JOIN FETCH e.course ORDER BY e.enrolledAt DESC")
    List<Enrollment> findAllEnrollmentsWithDetails();
    
    // Count enrollments for a course
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.cid = :courseId AND e.enrollmentStatus = 'ACTIVE'")
    Long countActiveByCourseId(@Param("courseId") int courseId);
    
    // Find recent enrollments
    @Query("SELECT e FROM Enrollment e JOIN FETCH e.user JOIN FETCH e.course ORDER BY e.enrolledAt DESC LIMIT 10")
    List<Enrollment> findRecentEnrollments();
}