package com.learning.learningSpring.model.dao;

import java.util.List;

import com.learning.learningSpring.entity.Comment;
import com.learning.learningSpring.entity.Post;

import lombok.Data;

@Data

public class PostDetailResponse {

    private Post post;

    private String userName;

    private List<Comment> commentList;

    private int likeCount;

    public PostDetailResponse(Post post, String userName, List<Comment> commentList, int likeCount) {

        this.post = post;

        this.userName = userName;

        this.commentList = commentList;

        this.likeCount = likeCount;

    }

}
