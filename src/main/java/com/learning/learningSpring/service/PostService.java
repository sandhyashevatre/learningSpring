package com.learning.learningSpring.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.learning.learningSpring.entity.Post;
import com.learning.learningSpring.repository.PostRepository;

@Service

public class PostService {

    @Autowired

    private PostRepository postRepository;

    public Post getPostsById(int postId) {

        return postRepository.findById(postId).get();

    }

}
