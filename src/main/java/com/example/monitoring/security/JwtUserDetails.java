package com.example.monitoring.security;

import com.example.monitoring.dao.interfaces.UserRepositoryInterface;
import com.example.monitoring.exceptions.UserNotFoundException;
import com.example.monitoring.model.Role;
import com.example.monitoring.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetails implements UserDetailsService {
	private UserRepositoryInterface userRepositoryInterface;
	@Autowired
	public JwtUserDetails(UserRepositoryInterface userRepositoryInterface) {
		this.userRepositoryInterface = userRepositoryInterface;
	}

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		final User user = userRepositoryInterface.findByLogin(login);

		if (user == null) {
			throw new UserNotFoundException("User '" + login + "' not found");
		}

		return org.springframework.security.core.userdetails.User
				.withUsername(user.getLogin())
				.password(user.getPassword())
				.authorities(Role.ROLE_CLIENT)
				.accountLocked(false)
				.credentialsExpired(false)
				.disabled(false)
				.build();
	}

}
