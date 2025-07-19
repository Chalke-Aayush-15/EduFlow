// Updated Course Entity with OneToMany mapping
package com.example.course.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;

@Entity(name = "Course") 
@EntityListeners(AuditingEntityListener.class)
public class course {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cid;
	
	@Lob
	private String imgurl;
	
	private String cname;
	private String ctutor;
	private String cdesc;
	private String cduration;
	private Double cprice;
	private int cenrollment;
	private String Status;
	
	@CreatedDate
    private Instant createdAt;
    
    // OneToMany mapping: One course can have many enrollments
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Enrollment> enrollments = new ArrayList<>();
	
	// Constructors
	public course() {
		super();
	}
	
	public course(int cid, String imgurl, String cname, String ctutor, String cdesc, String cduration, Double cprice,
			int cenrollment, String status, Instant createdAt) {
		super();
		this.cid = cid;
		this.imgurl = imgurl;
		this.cname = cname;
		this.ctutor = ctutor;
		this.cdesc = cdesc;
		this.cduration = cduration;
		this.cprice = cprice;
		this.cenrollment = cenrollment;
		Status = status;
		this.createdAt = createdAt;
	}
	
	// Helper methods for managing enrollments
	public void addEnrollment(Enrollment enrollment) {
		enrollments.add(enrollment);
		enrollment.setCourse(this);
	}
	
	public void removeEnrollment(Enrollment enrollment) {
		enrollments.remove(enrollment);
		enrollment.setCourse(null);
	}
	
	public int getActualEnrollmentCount() {
		return enrollments.size();
	}
	
	// All existing getters and setters
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getCtutor() {
		return ctutor;
	}
	public void setCtutor(String ctutor) {
		this.ctutor = ctutor;
	}
	public String getCdesc() {
		return cdesc;
	}
	public void setCdesc(String cdesc) {
		this.cdesc = cdesc;
	}
	public String getCduration() {
		return cduration;
	}
	public void setCduration(String cduration) {
		this.cduration = cduration;
	}
	public Double getCprice() {
		return cprice;
	}
	public void setCprice(Double cprice) {
		this.cprice = cprice;
	}
	public int getCenrollment() {
		return cenrollment;
	}
	public void setCenrollment(int cenrollment) {
		this.cenrollment = cenrollment;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public Instant getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	
	// New getter and setter for enrollments
	public List<Enrollment> getEnrollments() {
		return enrollments;
	}
	public void setEnrollments(List<Enrollment> enrollments) {
		this.enrollments = enrollments;
	}
	
	@Override
	public String toString() {
		return "course [cid=" + cid + ", imgurl=" + imgurl + ", cname=" + cname + ", ctutor=" + ctutor + ", cdesc="
				+ cdesc + ", cduration=" + cduration + ", cprice=" + cprice + ", cenrollment=" + cenrollment
				+ ", Status=" + Status + ", createdAt=" + createdAt + ", enrollmentCount=" + enrollments.size() + "]";
	}
}