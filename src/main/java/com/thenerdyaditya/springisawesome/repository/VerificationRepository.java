package com.thenerdyaditya.springisawesome.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thenerdyaditya.springisawesome.model.VerificationToken;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationToken, Long>{
	
	Optional<VerificationToken> findByToken(String token);
	
}
