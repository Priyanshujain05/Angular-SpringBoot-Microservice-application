package com.open.auth.repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.open.auth.model.Role;
import com.open.auth.model.User;

@Component
public class InMemoryUserRepository implements UserRepository {
	private final List<User> users;

	public InMemoryUserRepository() {
		users = new ArrayList<>();
		users.add(new User("admin", "adminPass", (Role.ADMIN)));
		users.add(new User("expertUser", "expertPass", Role.EXPERT_USER));
	}

	@Override
	public User findByUsername(String username) {
		return users.stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null);
	}
}
