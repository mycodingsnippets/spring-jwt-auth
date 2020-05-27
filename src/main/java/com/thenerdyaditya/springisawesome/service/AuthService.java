package com.thenerdyaditya.springisawesome.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thenerdyaditya.springisawesome.dto.AuthenticationResponse;
import com.thenerdyaditya.springisawesome.dto.LoginRequest;
import com.thenerdyaditya.springisawesome.dto.RefreshTokenRequest;
import com.thenerdyaditya.springisawesome.dto.RegisterRequest;
import com.thenerdyaditya.springisawesome.exceptions.SpringRedditException;
import com.thenerdyaditya.springisawesome.model.User;
import com.thenerdyaditya.springisawesome.model.VerificationToken;
import com.thenerdyaditya.springisawesome.repository.UserRepository;
import com.thenerdyaditya.springisawesome.repository.VerificationRepository;
import com.thenerdyaditya.springisawesome.security.JwtProvider;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
	
	private final PasswordEncoder passwordEncoder;
	
	private final UserRepository userRepository;
	
	private final VerificationRepository verificationRepository;
	
	private final AuthenticationManager authenticationManager;
	
	private final JwtProvider jwtProvider;
	
	private final RefreshTokenService refreshTokenService;

	@Transactional
	public void signup(RegisterRequest registerRequest) {
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setCreated(Instant.now());
		user.setEnabled(false);
		
		userRepository.save(user);
		
		String token = generateVerificationToken(user);
		testAsync(token);
	}
	
	@Async
	private void testAsync(String token) {
		System.out.println(token);
	}

	private String generateVerificationToken(User user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		
		verificationRepository.save(verificationToken);
		return token;
	}
	
	public void verifyToken(String token) {
		Optional<VerificationToken> verificationToken = verificationRepository.findByToken(token);
		verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
		fetchUserAndEnable(verificationToken.get());
	}

	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) {
		String username = verificationToken.getUser().getUsername();
		User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User not found with name - " + username));
		user.setEnabled(true);
		userRepository.save(user);
	}
	
	public AuthenticationResponse login(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
		);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtProvider.generateToken(authentication);
		
		return AuthenticationResponse.builder()
				.authenticationToken(token)
				.refreshToken(refreshTokenService.generateRefreshToken().getToken())
				.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
				.username(loginRequest.getUsername())
				.build();
	}

	public AuthenticationResponse refreshToken(@Valid RefreshTokenRequest refreshTokenRequest) {
		refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
		String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
		return AuthenticationResponse.builder()
				.authenticationToken(token)
				.refreshToken(refreshTokenRequest.getRefreshToken())
				.expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
				.username(refreshTokenRequest.getUsername())
				.build();
	}
	
}
