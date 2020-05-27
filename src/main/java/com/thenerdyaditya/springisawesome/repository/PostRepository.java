package com.thenerdyaditya.springisawesome.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thenerdyaditya.springisawesome.model.Post;
import com.thenerdyaditya.springisawesome.model.Subreddit;
import com.thenerdyaditya.springisawesome.model.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
	List<Post> findAllBySubreddit(Subreddit subreddit);
	List<Post> findByUser(User user);
}
