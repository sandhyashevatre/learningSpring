package com.learning.learningSpring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.learningSpring.entity.Comment;
import com.learning.learningSpring.entity.User;
import com.learning.learningSpring.model.dao.CommentDTO;
import com.learning.learningSpring.repository.CommentRepository;

@Service

public class CommentService {

    @Autowired

    private CommentRepository commentRepository;

    @Autowired

    private PostService postservice;

    public Comment createComment(CommentDTO commentDTO, User user) {

        Comment comment = new Comment();

        comment.setContent(commentDTO.getContent());

        comment.setPost(postservice.getPostsById(commentDTO.getPostId()));

        comment.setUser(user);

        return commentRepository.save(comment);

    }

}
