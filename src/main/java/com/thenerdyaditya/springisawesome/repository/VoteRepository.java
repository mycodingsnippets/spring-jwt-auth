package com.thenerdyaditya.springisawesome.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thenerdyaditya.springisawesome.model.Post;
import com.thenerdyaditya.springisawesome.model.User;
import com.thenerdyaditya.springisawesome.model.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long>{

	Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
