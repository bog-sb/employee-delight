package com.up.employeedelight.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.up.employeedelight.domain.Vote;

public interface VoteRepository extends CrudRepository<Vote, Integer> {

	@Query("update Vote v set v.isSubmitted = 1")
	public void setAllSubmitted();
}
