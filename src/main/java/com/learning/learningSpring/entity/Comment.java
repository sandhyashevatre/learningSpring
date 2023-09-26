package com.learning.learningSpring.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Comment")
@Data
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "postId", referencedColumnName = "id")
    private Post post;

	private String content;

    private int likes;

    private boolean likedByUser;

    public void setText(String text){
        
    }

    @CreationTimestamp
    @Column(name = "timestamp")
    private Date timestamp;

    @UpdateTimestamp
    private Date updatedAt;



}
