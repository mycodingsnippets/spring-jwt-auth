package com.thenerdyaditya.springisawesome.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thenerdyaditya.springisawesome.dto.AuthenticationResponse;
import com.thenerdyaditya.springisawesome.dto.LoginRequest;
import com.thenerdyaditya.springisawesome.dto.RefreshTokenRequest;
import com.thenerdyaditya.springisawesome.dto.RegisterRequest;
import com.thenerdyaditya.springisawesome.service.AuthService;
import com.thenerdyaditya.springisawesome.service.RefreshTokenService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	private final RefreshTokenService refreshTokenService;
	
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
		authService.signup(registerRequest);
		return new ResponseEntity<>("User Registration Successful", HttpStatus.OK);
	}
	
	@GetMapping("/account-verification/{token}")
	public ResponseEntity<String> verifyAccount(@PathVariable String token){
		authService.verifyToken(token);
		return new ResponseEntity<>("Account activated Successfully", HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
		return authService.login(loginRequest);
	}
	
	@PostMapping("/refresh/token")
	public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
		return authService.refreshToken(refreshTokenRequest);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
		refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
		return ResponseEntity
				.status(HttpStatus.OK)
				.body("Refresh Token Deleted Successfully");
	}
}
