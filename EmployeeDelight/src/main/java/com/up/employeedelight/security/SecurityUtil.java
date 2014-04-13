package com.up.employeedelight.security;

import java.security.MessageDigest;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Component;

import com.up.employeedelight.domain.User;

/**
 * Security utils.
 * 
 * @author bogdan.sbiera
 */
@Component
public final class SecurityUtil {
	/** ROLE_USER */
	public static final String ROLE_USER = "ROLE_USER";

	/** ROLE_ADMIN */
	public static final String ROLE_ADMIN = "ROLE_ADMIN";

	/** The strength of the SHA algorithm. */
	private static final int SHA_STRENGTH = 256;

	/** Max length. */
	private static final int MAX_LENGTH = 20;

	/**
	 * Constructor.
	 */
	private SecurityUtil() {
	}

	/**
	 * Hashes the password merged with the salt.Uses SHA-256 for encoding.
	 * 
	 * @note if the hashing method is changed, all the password hashes in the
	 *       database should be reset
	 * @param password
	 *            String
	 * @param salt
	 *            String
	 * @return hashed password
	 */
	public static String hashPassword(final String password, final String salt) {
		final ShaPasswordEncoder encoder = new ShaPasswordEncoder(SHA_STRENGTH);
		final String hash = encoder.encodePassword(password, salt);
		return hash;
	}

	/**
	 * Generates a password validation token for the user based on some of his
	 * fields.Uses SHA-256 for encoding.
	 * 
	 * @param user
	 *            User
	 * @return encoded string null if the encoding algorithm is not found
	 */
	public static String generateToken(final User user) {
		MessageDigest md;
		String token = null;
		// final int radix = 64;
		// try {
		// md = MessageDigest.getInstance("SHA-256");
		// final String hashData = user.getSalt() + user.getUserId()
		// + user.getUsername() + new Date().toString();
		//
		// final byte[] bytes = hashData.getBytes();
		// token = new BigInteger(1, md.digest(bytes)).toString(radix)
		// .substring(0, MAX_LENGTH);
		//
		// } catch (final NoSuchAlgorithmException e) {
		// e.printStackTrace();
		// }
		return token;
	}

	/**
	 * Generates a token that will be used for authentication after a successful
	 * login.
	 * 
	 * @return token
	 */
	@SuppressWarnings("deprecation")
	public static String generateAuthenticationToken() {
		// TODO
		String token = "token";
		token = token + new Date().getSeconds(); // just a mock
		return token;
	}

	/**
	 * Returns a collection of the authorities of the user. (An authority is an
	 * Operation name)
	 * 
	 * @param user
	 *            User
	 * @return Collection(Set) of authorities
	 */

	public static Collection<GrantedAuthorityImpl> getAuthorities(final User user) {
		final Set<GrantedAuthorityImpl> authorities = new HashSet<GrantedAuthorityImpl>();
		if (user.getIsAdmin()) {
			authorities.add(new GrantedAuthorityImpl(ROLE_ADMIN));
		} else {
			authorities.add(new GrantedAuthorityImpl(ROLE_USER));
		}
		return authorities;
	}

}
