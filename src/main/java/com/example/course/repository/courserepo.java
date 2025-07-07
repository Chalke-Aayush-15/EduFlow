package com.example.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.course.entity.course;

public interface courserepo extends JpaRepository<course, Integer>{
	
	
}
