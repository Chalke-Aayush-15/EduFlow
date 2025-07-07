package com.example.course.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.course.entity.course;
import com.example.course.entity.userentity;
import com.example.course.repository.courserepo;
import com.example.course.repository.userrepo;

@Service
public class courseservice {

	@Autowired
	private courserepo cr;
	@Autowired
	private userrepo ur;
	
	public List<course> getcourse() {
		// TODO Auto-generated method stub
		return cr.findAll();
	}

	public void submitcourse(course course) {
		// TODO Auto-generated method stub
		cr.save(course);
	}

	public void deletebyid(int id) {
		// TODO Auto-generated method stub
		cr.deleteById(id);
	}

	public void updatecourse(course course) {
		// TODO Auto-generated method stub
		cr.save(course);
	}

	public course getcoursebyid(int id) {
		// TODO Auto-generated method stub
		return cr.findById(id).orElse(null);
	}

	public Object gettotalcourse() {
		// TODO Auto-generated method stub
		return cr.count();
	}

	public Object gettotalstudent() {
		// TODO Auto-generated method stub
		return ur.totalstudent();
	}

	public Object gettotalinstructor() {
		// TODO Auto-generated method stub
		return ur.totalinstructor();
	}

	public Object gettotaladmin() {
		// TODO Auto-generated method stub
		return ur.totaladmin();
	}

	public Object gettotaluser() {
		// TODO Auto-generated method stub
		return ur.count();
	}

	public List<userentity> getusertable() {
	    List<userentity> users = ur.findAll();
	    for (userentity u : users) {
	        u.setInitials(u.getInitials());  // populate initials for each user
	    }
	    return users;
	}
	
	public List<userentity> getAllUsers() {
	    List<userentity> users = ur.findAll();
	    for (userentity u : users) {
	        u.setInitials(u.getInitials());
	    }
	    return users;
	}

	public List<userentity> getUsersByRole(String role) {
	    List<userentity> users = ur.findByRole(role);
	    for (userentity u : users) {
	        u.setInitials(u.getInitials());
	    }
	    return users;
	}

	public Object gettotalactivecourse() {
		// TODO Auto-generated method stub
		return ur.getTotalActiveCourse();
	}

	public course findById(int courseId) {
		// TODO Auto-generated method stub
		return cr.findById(courseId).orElse(null);
	}

	
	

//	public List<String> getfirstchar() {
//		// TODO Auto-generated method stub
////		return ur.take();
//		for ( String s : ur.take()) 
//		{
//			return "";
//		}
//	}
	
	

	
}
