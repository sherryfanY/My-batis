package com.jt.vo;

import java.io.Serializable;
import java.sql.Date;

import com.jt.entity.Author;

/**
 * valueObject 值对象
 * 
 * @author soft01
 *
 */

public class BlogResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String title;
	private String content;
	private Date createdTime;
	private Author author;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	@Override
	public String toString() {
		return "BlogAuthor [id=" + id + ", title=" + title + ", content=" + content + ", createdTime=" + createdTime
				+ ", author=" + author + "]";
	}

}
