package com.open.auth.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.open.auth.model.TokenResponse;
import com.open.auth.model.User;
import com.open.auth.repository.UserRepository;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Value("${jwt.secret}")
	private String secret;

	private final UserRepository userRepository;

	@Autowired
	public AuthController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@RequestBody User credentials) {
		User user = userRepository.findByUsername(credentials.getUsername());
		if (user != null && user.getPassword().equals(credentials.getPassword())) {
			Map<String, Object> claims = new HashMap<>();
			claims.put("roles", user.getRole());

			String token = Jwts.builder().setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
					.setSubject(user.getUsername()).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60))
					.signWith(SignatureAlgorithm.HS256, "3x@mpl3S3cr3tK3y#2025^#JWT!").compact();

			return ResponseEntity.ok(new TokenResponse(token));
		}
		throw new JwtException("Invalid login credentials or token generation failed.");
	}
}
