package com.encrypted.secured.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.encrypted.secured.cipher.EncryptionUtils;
import com.encrypted.secured.service.MatchService;

@RestController
@RequestMapping("/matches")
public class MatchController {

	private final MatchService matchService;

	public MatchController(MatchService matchService) {
		this.matchService = matchService;
	}

	@PreAuthorize("hasRole('ROLE_EXPERT_USER')")
	@GetMapping("/draws")
	public ResponseEntity<String> getDrawMatches(@RequestParam("year")  String year) throws Exception {
		System.out.println("Encrypted year received: " + year);
		int decryptedYear = Integer.parseInt(EncryptionUtils.decrypt(year));
		CompletableFuture<Integer> futureDrawCount = matchService.getDrawMatchesCount(decryptedYear);

		int drawCount = futureDrawCount.get(); 
		return ResponseEntity.ok(EncryptionUtils.encrypt(String.valueOf(drawCount)));
	}
}
