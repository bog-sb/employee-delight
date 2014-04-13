package com.up.employeedelight.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.up.employeedelight.domain.Product;
import com.up.employeedelight.domain.User;
import com.up.employeedelight.domain.Vote;
import com.up.employeedelight.repository.ProductRepository;
import com.up.employeedelight.repository.UserRepository;
import com.up.employeedelight.repository.VoteRepository;

@Service
public class VoteManager {

	@Autowired
	private VoteRepository voteRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ProductRepository productRepo;

	public Integer submitVote(Integer productId, Integer points) {
		// create and save vote
		User user = userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		if (user.getPoints() > points) {
			Product product = productRepo.findOne(productId);
			Vote newVote = new Vote(new Date(), points, user, product);
			voteRepo.save(newVote);

			// update the user's points
			user.setPoints(user.getPoints() - points);
			userRepo.save(user);
			return user.getPoints();
		} else {
			return -1;
		}
	}
}
