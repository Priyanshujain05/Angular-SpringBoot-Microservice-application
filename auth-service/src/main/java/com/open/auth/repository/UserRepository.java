package com.open.auth.repository;

import com.open.auth.model.User;

public interface UserRepository {
	User findByUsername(String username);
}
