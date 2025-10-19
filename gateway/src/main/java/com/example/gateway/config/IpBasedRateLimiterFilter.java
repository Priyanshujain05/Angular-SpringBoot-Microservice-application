package com.example.gateway.config;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.google.common.util.concurrent.RateLimiter;

import reactor.core.publisher.Mono;

@Component
public class IpBasedRateLimiterFilter implements GlobalFilter, Ordered {

	private final ConcurrentHashMap<String, RateLimiter> limiters = new ConcurrentHashMap<>();

	private RateLimiter getRateLimiter(String ip) {
		return limiters.computeIfAbsent(ip, k -> RateLimiter.create(1.0)); // 1 request/sec limit
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String clientIp = exchange.getRequest().getRemoteAddress() != null
				? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
				: "unknown";

		RateLimiter limiter = getRateLimiter(clientIp);
		boolean allowed = limiter.tryAcquire();

		if (!allowed) {
			exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
			return exchange.getResponse().setComplete();
		}

		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return -1;
	}
}