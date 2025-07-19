package com.example.course.service;

import java.io.PrintWriter;
import java.util.List;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.course.entity.course;
import com.example.course.entity.userentity;
import com.example.course.repository.courserepo;
import com.example.course.repository.userrepo;

import jakarta.servlet.http.HttpServletResponse;

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
		return cr.getTotalActiveCourse();
	}

	public course findById(int courseId) {
		// TODO Auto-generated method stub
		return cr.findById(courseId).orElse(null);
	}

	public void exportCoursesToCSV(HttpServletResponse response) throws IOException {
        List<course> courses = cr.findAll();
        
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"courses_export.csv\"");
        
        try (PrintWriter writer = response.getWriter()) {
            // CSV Header
            writer.println("Course ID,Course Name,Instructor,Students Enrolled,Created Date,Status,Description,Duration,Price");
            
            // CSV Data
            for (course course : courses) {
                StringBuilder csvLine = new StringBuilder();
                csvLine.append(quote(String.valueOf(course.getCid()))).append(",");
                csvLine.append(quote(course.getCname())).append(",");
                csvLine.append(quote(course.getCtutor())).append(",");
                csvLine.append(quote(String.valueOf(course.getCenrollment()))).append(",");
                csvLine.append(quote(course.getCreatedAt() != null ? course.getCreatedAt().toString() : "")).append(",");
                csvLine.append(quote(course.getStatus())).append(",");
                csvLine.append(quote(course.getCdesc() != null ? course.getCdesc() : "")).append(",");
                csvLine.append(quote(course.getCduration() != null ? course.getCduration() : "")).append(",");
                csvLine.append(quote(course.getCprice() != null ? course.getCprice().toString() : ""));
                
                writer.println(csvLine.toString());
            }
        }
    }
    
    // Helper method to properly quote CSV values
    private String quote(String value) {
        if (value == null) {
            return "\"\"";
        }
        
        // Escape quotes and wrap in quotes
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
    
    // Alternative method that returns CSV as String (useful for other purposes)
    public String generateCSVString() {
        List<course> courses = cr.findAll();
        StringBuilder csvContent = new StringBuilder();
        
        // Header
        csvContent.append("Course ID,Course Name,Instructor,Students Enrolled,Created Date,Status,Description,Duration,Price\n");
        
        // Data
        for (course course : courses) {
            csvContent.append(quote(String.valueOf(course.getCid()))).append(",");
            csvContent.append(quote(course.getCname())).append(",");
            csvContent.append(quote(course.getCtutor())).append(",");
            csvContent.append(quote(String.valueOf(course.getCenrollment()))).append(",");
            csvContent.append(quote(course.getCreatedAt() != null ? course.getCreatedAt().toString() : "")).append(",");
            csvContent.append(quote(course.getStatus())).append(",");
            csvContent.append(quote(course.getCdesc() != null ? course.getCdesc() : "")).append(",");
            csvContent.append(quote(course.getCduration() != null ? course.getCduration() : "")).append(",");
            csvContent.append(quote(course.getCprice() != null ? course.getCprice().toString() : ""));
            csvContent.append("\n");
        }
        
        return csvContent.toString();
    }
	
	

	
}
