package com.bookkeeping.backend.apis.datafetcher;

import com.bookkeeping.backend.apis.model.Author;
import com.bookkeeping.backend.apis.model.Book;
import com.bookkeeping.backend.apis.repository.AuthorRepository;
import com.bookkeeping.backend.apis.repository.BookRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DgsComponent
public class BookDataFetcher {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@DgsQuery
	public List<Book> books() {
		return bookRepository.findAll();
	}

	@DgsMutation
	public Book addBook(String title, Long authorId) {
		Author author = authorRepository.findById(authorId).orElseThrow(() -> new RuntimeException("Author not found"));
		Book book = new Book();
		book.setTitle(title);
		book.setAuthor(author);
		return bookRepository.save(book);
	}
}
