package com.hancy.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hancy.app.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
