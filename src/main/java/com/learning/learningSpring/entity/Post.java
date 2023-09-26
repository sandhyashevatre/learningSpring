package com.learning.learningSpring.entity;

import java.util.Date;
import java.util.HashSet;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.mapping.Set;
import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Component
@Data
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String content;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "author_id", referencedColumnName = "id")
	private User author;

	@CreationTimestamp
	@Column(name = "timestamp")
	private Date timestamp;

	@UpdateTimestamp
	private Date updatedAt;

	// @ManyToMany
    // @JoinTable(name = "post_tag",
    //            joinColumns = @JoinColumn(name = "post_id"),
    //            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    // private Set<Tag> tags = new HashSet<>();
}
