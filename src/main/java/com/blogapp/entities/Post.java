package com.blogapp.entities;


import java.util.Date;
import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Posts")
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String title;
	private String excerpt;
	private String content;
	private String author;
	@Column(name = "published_at")
	private Date publishedAt;
	@Column(name = "is_published")
	private String isPublished;
	@Column(name = "created_at")
	private Date createdAt;
	@Column(name = "updated_at")
	private Date updatedAt;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<Comment> comments;
	
	
	public Post() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Post(int id, String title, String excerpt, String content, String author, Date publishedAt,
			String isPublished, Date createdAt, Date updatedAt, User user, List<Tag> tags, List<Comment> comments) {
		super();
		this.id = id;
		this.title = title;
		this.excerpt = excerpt;
		this.content = content;
		this.author = author;
		this.publishedAt = publishedAt;
		this.isPublished = isPublished;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.user = user;
		this.tags = tags;
		this.comments = comments;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getExcerpt() {
		return excerpt;
	}


	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public Date getPublishedAt() {
		return publishedAt;
	}


	public void setPublishedAt(Date publishedAt) {
		this.publishedAt = publishedAt;
	}


	public String getIsPublished() {
		return isPublished;
	}


	public void setIsPublished(String isPublished) {
		this.isPublished = isPublished;
	}


	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}


	public Date getUpdatedAt() {
		return updatedAt;
	}


	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public List<Tag> getTags() {
		return tags;
	}


	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}


	public List<Comment> getComments() {
		return comments;
	}


	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}


	
	
	
}
