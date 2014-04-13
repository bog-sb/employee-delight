package com.up.employeedelight.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.up.employeedelight.domain.User;
import com.up.employeedelight.repository.UserGroupRepository;
import com.up.employeedelight.repository.UserRepository;

@Service
public class UserGroupManager {
	@Autowired
	UserGroupRepository userGroupRepo;

	@Autowired
	UserRepository userRepo;

	public int getGroupId(String name) {
		int i = 0;
		i =	userGroupRepo.findByName(name).getId();
		System.out.println("===================" + i);
		return i;
	}

	public void updateGroupPoints(String groupName, Integer points) {
		Iterable<User> users = userRepo.findAll();
		for(User u : users){
			if(u.getUserGroup().getName().equals(groupName)){
				u.setPoints(points);
			}
		}
		userRepo.save(users);
	}
}
