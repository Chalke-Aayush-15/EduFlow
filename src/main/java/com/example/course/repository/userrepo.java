package com.example.course.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.course.entity.userentity;

@Repository
public interface userrepo extends JpaRepository<userentity, Integer>{

	userentity findByEmailAndPassword(String email, String password);
	
	@Query("select count(role) from userentity where role=('USER')")
	int totalstudent();
	
	@Query("select count(role) from userentity where role=('INSTRUCTOR')")
	int totalinstructor();
	
	@Query("select count(role) from userentity where role=('ADMIN')")
	int totaladmin();
	
	List<userentity> findByRole(String role);
	
	 userentity findByEmail(String email);

	 



	boolean existsByEmail(String email);



	

}
