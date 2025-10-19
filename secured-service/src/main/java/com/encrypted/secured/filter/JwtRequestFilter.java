package com.encrypted.secured.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader("Authorization");

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String jwt = authorizationHeader.substring(7);
			System.out.println("Received JWT: " + jwt);
			String username = jwtUtil.extractUsername(jwt);
			System.out.println("Extracted Username: " + username);

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				if (jwtUtil.validateToken(jwt, username)) {
					System.out.println("Token is valid");
					String role = jwtUtil.extractRole(jwt);
					List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							username, null, authorities);

					SecurityContextHolder.getContext().setAuthentication(authentication);
				} else {
					System.out.println("Token is invalid");
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
					return;
				}
			}
		}
		chain.doFilter(request, response);
	}

}
