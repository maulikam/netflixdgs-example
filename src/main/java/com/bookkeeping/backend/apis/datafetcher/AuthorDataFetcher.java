package com.bookkeeping.backend.apis.datafetcher;

import com.bookkeeping.backend.apis.model.Author;
import com.bookkeeping.backend.apis.repository.AuthorRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DgsComponent
public class AuthorDataFetcher {

	@Autowired
	private AuthorRepository authorRepository;

	@DgsQuery
	public List<Author> authors() {
		return authorRepository.findAll();
	}

	@DgsMutation
	public Author addAuthor(String name) {
		Author author = new Author();
		author.setName(name);
		return authorRepository.save(author);
	}
}
