package com.up.employeedelight.repository;

import org.springframework.data.repository.CrudRepository;

import com.up.employeedelight.domain.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	public User findByUsername(String username);
	public User findByEmail(String email);
}