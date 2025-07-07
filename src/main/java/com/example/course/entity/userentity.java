// Updated User Entity with OneToMany mapping
package com.example.course.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class userentity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String fname;
	private String lname;
	
	@Column(unique = true)
	private String email;
	private String phno;
	private String password;
	private String role;
	private String status = "Active";
	
	@CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant modifiedAt;
    
    @Transient
	private String initials;
    
    // OneToMany mapping: One user can have many enrollments
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Enrollment> enrollments = new ArrayList<>();

	// Constructors
	public userentity() {
		super();
	}
	
	public userentity(int id, String fname, String lname, String email, String phno, String password, String role,
			String status, Instant createdAt, Instant modifiedAt, String initials, List<Enrollment> enrollments) {
		super();
		this.id = id;
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.phno = phno;
		this.password = password;
		this.role = role;
		this.status = status;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.initials = initials;
		this.enrollments = enrollments;
	}



	// Helper methods for managing enrollments
	public void addEnrollment(Enrollment enrollment) {
		enrollments.add(enrollment);
		enrollment.setUser(this);
	}
	
	public void removeEnrollment(Enrollment enrollment) {
		enrollments.remove(enrollment);
		enrollment.setUser(null);
	}
	
	// All existing getters and setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Instant getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	public Instant getModifiedAt() {
		return modifiedAt;
	}
	public void setModifiedAt(Instant modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
	
	// New getter and setter for enrollments
	public List<Enrollment> getEnrollments() {
		return enrollments;
	}
	public void setEnrollments(List<Enrollment> enrollments) {
		this.enrollments = enrollments;
	}
	
	public String getInitials() {
	    if (fname != null && lname != null) {
	        return fname.substring(0, 1).toUpperCase() + lname.substring(0, 1).toUpperCase();
	    }
	    return "";
	}
	
	public void setInitials(String initials) {
	    this.initials = initials;
	}

	@Override
	public String toString() {
		return "userentity [id=" + id + ", fname=" + fname + ", lname=" + lname + ", email=" + email + ", phno=" + phno
				+ ", password=" + password + ", role=" + role + ", status=" + status + ", createdAt=" + createdAt
				+ ", modifiedAt=" + modifiedAt + ", initials=" + initials + ", enrollments=" + enrollments + "]";
	}

	public String getPhno() {
		return phno;
	}

	public void setPhno(String phno) {
		this.phno = phno;
	}
}