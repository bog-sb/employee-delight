package com.up.employeedelight.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.up.employeedelight.domain.User;
import com.up.employeedelight.exceptions.EDIllegalOperation;
import com.up.employeedelight.repository.UserRepository;

@Service
public class UserManager {
	@Autowired
	UserRepository userRepo;

	@Autowired
	UserGroupManager groupMan;

	public void updateUser(User user) {
		User userDb = userRepo.findOne(user.getId());
		if (!(null != user.getPassword() || user.getPassword().equals(""))) {
			userDb.setPassword(user.getPassword());
		}
		if (!(null != user.getEmail() || user.getEmail().equals(""))) {
			userDb.setEmail(user.getEmail());
		}
		if (!(null != user.getUsername() || user.getUsername().equals(""))) {
			userDb.setUsername(user.getUsername());
		}
		if (!(null != user.getName() || user.getName().equals(""))) {
			userDb.setName(user.getName());
		}
		if (!(null != user.getUserGroup() || user.getUserGroup().equals(""))) {
			userDb.getUserGroup().setName(user.getUserGroup().getName());
			userDb.getUserGroup().setId(groupMan.getGroupId(user.getUserGroup().getName()));
		}
		user = new User();
		userDb.setIsAdmin(user.getIsAdmin());

		userRepo.save(userDb);
	}

	public void saveUser(User user) {
		userRepo.save(user);
	}

	public void deleteUser(User user) throws EDIllegalOperation {
		if (userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId() != user
				.getId()) {
			userRepo.delete(user.getId());
		} else {
			throw new EDIllegalOperation("You cannot delete your own account.");
		}
	}

	public List<User> getUsers() {
		return Lists.newArrayList(userRepo.findAll());
	}

	public void addUser(User user) throws EDIllegalOperation {
		if (null == userRepo.findByUsername(user.getName()) || null == userRepo.findByUsername(user.getEmail())
				|| "".equals(userRepo.findByUsername(user.getName()))
				|| "".equals(userRepo.findByEmail(user.getEmail()))) {
			user.setSalt("");
			user.getUserGroup().setId(groupMan.getGroupId(user.getUserGroup().getName()));
			userRepo.save(user);
		} else {
			throw new EDIllegalOperation("Email or Usernameallready exists!");
		}
	}

	public User getUserByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	public void removeUser(int id)throws EDIllegalOperation {
		if (userRepo.findOne(id) != null) {
			userRepo.delete(id);
		}
	}
	
}
