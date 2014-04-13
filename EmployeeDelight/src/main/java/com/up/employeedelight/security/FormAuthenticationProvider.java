package com.up.employeedelight.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.up.employeedelight.domain.User;
import com.up.employeedelight.repository.UserRepository;

public class FormAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	private UserRepository userRepo;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String userOrEmail = authentication.getName();
		String pass = (String) authentication.getCredentials();
		String hashedPassword = null;
		Boolean validAuth = false;

		User user = null;

		// check if user tries to login using email
		if (userOrEmail.contains("@")) {
			user = userRepo.findByEmail(userOrEmail);
		} else {
			user = userRepo.findByUsername(userOrEmail);
		}

		if (user != null) {
			// check authentication password against user's password

			/*
			 * TODO: hash password ; // hashedPassword =
			 * SecurityUtil.hashPassword(pass, user.getSalt()); if
			 * (hashedPassword.equals(user.getPasswordHash())) { validAuth =
			 * true; }
			 */
			if (pass != null && user.getPassword().equals(pass)) {
				validAuth = true;
			}
		}

		if (validAuth) {
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getUsername(),
					"***", SecurityUtil.getAuthorities(user));

			return authToken;
		} else {
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
