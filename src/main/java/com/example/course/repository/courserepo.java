package com.example.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.course.entity.course;

public interface courserepo extends JpaRepository<course, Integer>{
	
	@Query("SELECT COUNT(c) FROM Course c WHERE c.Status = 'Active'")
	 int getTotalActiveCourse();
	
}
