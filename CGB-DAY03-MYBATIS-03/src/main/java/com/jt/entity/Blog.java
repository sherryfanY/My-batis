package com.jt.entity;

import java.util.Date;


public class Blog {
	private Integer id;
	private String title;
	private String content;
	private Date createdTime;
	
	@Override
	public String toString() {
		return "Blog [id=" + id + ", tile=" + title + ", content=" + content + ", createdTime=" + createdTime + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String tile) {
		this.title = tile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	
}
