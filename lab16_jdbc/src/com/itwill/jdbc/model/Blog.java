package com.itwill.jdbc.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

//MVC 아키텍쳐에서 Model을 담당하는 객체.
public class Blog {
	
	//데이터베이스 테이블의 테이블이름,컬럼 이름들을 상수로 정의하는 내부 클래스.
	public static final class Entity {
		public static final String TBL_BLOGS = "BLOGS";
		public static final String COL_ID = "ID";
		public static final String COL_TITLE = "TITLE";
		public static final String COL_CONTENT = "CONTENT";
		public static final String COL_AUTHOR="AUTHOR";
		public static final String COL_CREATED_TIME = "CREATED_TIME";
		public static final String COL_MODIFIED_TIME = "MODIFIED_TIME";
	}
	
	private Integer id; // pk
	private String title;
	private String content;
	private String author;
	private LocalDateTime createdTime;
	private LocalDateTime modifiedTime;

	public Blog() {
	}

	public Blog(Integer id, String title, String content, String author, LocalDateTime createdTime,
			LocalDateTime modifiedTime) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.author = author;
		this.createdTime = createdTime;
		this.modifiedTime = modifiedTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public LocalDateTime getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(LocalDateTime modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public Integer getId() {
		return id;
	}

	// Builder 디자인 패턴
	public static BlogBuilder builder() {
		return new BlogBuilder();
	}

	public static class BlogBuilder {
		private Integer id; // pk
		private String title;
		private String content;
		private String author;
		private LocalDateTime createdTime;
		private LocalDateTime modifiedTime;
		
		public BlogBuilder id(int id) {
			this.id=id;
			return this;
		}	
		public BlogBuilder title(String title) {
			this.title = title;
			return this;
		}
		public BlogBuilder author(String author) {
			this.author = author;
			return this;
		}
		public BlogBuilder content(String content) {
			this.content = content;
			return this;
		}
		public BlogBuilder createdTime(LocalDateTime createdTime) { 
			this.createdTime=createdTime;
			return this;
		}
		
		public BlogBuilder createdTime(Timestamp createdTime) {
			//Timestamp 타입 객체를 LocalDateTime 타입 객체로 변환해서 저장.
			if (createdTime != null)
				this.createdTime=createdTime.toLocalDateTime();
			return this;
		}
		
		public BlogBuilder modifiedTime(LocalDateTime modifiedTime) { 
			this.modifiedTime=modifiedTime;
			return this;
		}
		public BlogBuilder modifiedTime(Timestamp modifiedTime) {
			//Timestamp 타입 객체를 LocalDateTime 타입 객체로 변환해서 저장.
			if (modifiedTime != null)
				this.modifiedTime=modifiedTime.toLocalDateTime();
			return this;
		}
		
		public Blog build() {
			return new Blog(id,title, content, author, createdTime, modifiedTime);
		}

	}

}
