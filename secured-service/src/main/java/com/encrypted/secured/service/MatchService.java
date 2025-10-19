package com.encrypted.secured.service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.encrypted.secured.model.Match;
import com.encrypted.secured.model.MatchResponse;

@Service
public class MatchService {

	private final RestTemplate restTemplate;
	private final Random random = new Random();

	public MatchService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Async("apiExecutor")
	public CompletableFuture<Integer> getDrawMatchesCount(int year) {
		return CompletableFuture.supplyAsync(() -> {
			String url = String.format("https://jsonmock.hackerrank.com/api/football_matches?year=%d&page=1", year);

			ResponseEntity<MatchResponse> response = restTemplate.getForEntity(url, MatchResponse.class);

			if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
				throw new RuntimeException("Failed to fetch data from the API");
			}

			MatchResponse matchResponse = response.getBody();
			List<Match> matches = matchResponse.getData();

			long totalDraws = matches.stream().filter(match -> match.getTeam1goals().equals(match.getTeam2goals()))
					.count();

			return (int) totalDraws;

		}, CompletableFuture.delayedExecutor(3 + random.nextInt(4), TimeUnit.SECONDS)); // Random delay between 3–6
																						// seconds

	}

}
