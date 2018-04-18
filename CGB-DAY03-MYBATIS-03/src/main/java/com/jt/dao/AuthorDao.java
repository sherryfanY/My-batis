package com.jt.dao;

import com.jt.entity.Author;

public interface AuthorDao {
	
	int updateAuthor(Author author);
	
	int insertAuthor(Author author);
	
	Author findAuthorById(String id);
}
