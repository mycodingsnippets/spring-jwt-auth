package com.thenerdyaditya.springisawesome.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thenerdyaditya.springisawesome.model.Comment;
import com.thenerdyaditya.springisawesome.model.Post;
import com.thenerdyaditya.springisawesome.model.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{

	List<Comment> findByPost(Post post);
	List<Comment> findAllByUser(User user);
	
}
