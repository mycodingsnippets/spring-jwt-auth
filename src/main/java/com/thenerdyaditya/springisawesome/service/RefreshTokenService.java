package com.thenerdyaditya.springisawesome.service;

import java.time.Instant;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.thenerdyaditya.springisawesome.exceptions.SpringRedditException;
import com.thenerdyaditya.springisawesome.model.RefreshToken;
import com.thenerdyaditya.springisawesome.repository.RefreshTokenRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;
	
	public RefreshToken generateRefreshToken() {
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setCreatedDate(Instant.now());
		
		return refreshTokenRepository.save(refreshToken);
	}
	
	public void validateRefreshToken(String token) {
		refreshTokenRepository.findByToken(token)
			.orElseThrow(() -> new SpringRedditException("Invalid Refresh Token"));
	}
	
	public void deleteRefreshToken(String token) {
		refreshTokenRepository.deleteByToken(token);
	}
	
}
