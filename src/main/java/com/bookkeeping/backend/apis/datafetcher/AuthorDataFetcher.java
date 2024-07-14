package com.bookkeeping.backend.apis.datafetcher;

import com.bookkeeping.backend.apis.model.Author;
import com.bookkeeping.backend.apis.repository.AuthorRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@DgsComponent
public class AuthorDataFetcher {

	@Autowired
	private AuthorRepository authorRepository;

	@DgsData(parentType = "Query", field = "authors")
	public List<Author> getAuthors() {
		return authorRepository.findAll();
	}

	@DgsData(parentType = "Query", field = "authorById")
	public Author getAuthorById(@InputArgument Long id) {
		return authorRepository.findById(id).orElse(null);
	}

	@DgsData(parentType = "Mutation", field = "addAuthor")
	public Author addAuthor(@InputArgument String name) {
		Author author = new Author();
		author.setName(name);
		return authorRepository.save(author);
	}

	@DgsData(parentType = "Mutation", field = "updateAuthor")
	public Author updateAuthor(@InputArgument Long id, @InputArgument String name) {
		Optional<Author> authorOptional = authorRepository.findById(id);
		if (authorOptional.isPresent()) {
			Author author = authorOptional.get();
			author.setName(name);
			return authorRepository.save(author);
		}
		return null;
	}

	@DgsData(parentType = "Mutation", field = "deleteAuthor")
	public String deleteAuthor(@InputArgument Long id) {
		authorRepository.deleteById(id);
		return "Author deleted successfully";
	}
}
