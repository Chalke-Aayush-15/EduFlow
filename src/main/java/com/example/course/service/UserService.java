package com.example.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.course.entity.userentity;
import com.example.course.repository.userrepo;

@Service
public class UserService {

	@Autowired
	private userrepo ur;
	
	public userentity findById(int userId) {
		// TODO Auto-generated method stub
		return ur.findById(userId).orElse(null);
	}

	public userentity updateuser(userentity use) {
		// TODO Auto-generated method stub
		 return ur.save(use);
	}

	  public userentity getUserByEmail(String email) {
	        return ur.findByEmail(email);
	    }

	public userentity getUserById(Integer id) {
        return ur.findById(id).orElse(null);
    }
	
	
	

	

}
