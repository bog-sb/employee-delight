package com.up.employeedelight.repository;

import org.springframework.data.repository.CrudRepository;

import com.up.employeedelight.domain.User;
import com.up.employeedelight.domain.UserGroup;

public interface UserGroupRepository extends CrudRepository<UserGroup, Integer> {
	public UserGroup findByName(String name);
}
