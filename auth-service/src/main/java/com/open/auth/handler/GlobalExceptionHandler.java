package com.open.auth.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.jsonwebtoken.JwtException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(JwtException.class)
	public ResponseEntity<Object> handleJwtException(JwtException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGeneralException(Exception ex) {
		System.out.println("Caught general exception: " + ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse("error", "An unexpected error occurred.", 500));
	}

	public static class ErrorResponse {
		private String status;
		private String message;
		private int code;

		public ErrorResponse(String status, String message, int code) {
			this.status = status;
			this.message = message;
			this.code = code;
		}
	}
}
