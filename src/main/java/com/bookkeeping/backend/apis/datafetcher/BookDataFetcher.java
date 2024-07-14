package com.bookkeeping.backend.apis.datafetcher;

import com.bookkeeping.backend.apis.model.Author;
import com.bookkeeping.backend.apis.model.Book;
import com.bookkeeping.backend.apis.repository.AuthorRepository;
import com.bookkeeping.backend.apis.repository.BookRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@DgsComponent
public class BookDataFetcher {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@DgsData(parentType = "Query", field = "books")
	public List<Book> getBooks() {
		return bookRepository.findAll();
	}

	@DgsData(parentType = "Query", field = "bookById")
	public Book getBookById(@InputArgument Long id) {
		return bookRepository.findById(id).orElse(null);
	}

	@DgsData(parentType = "Query", field = "booksByAuthor")
	public List<Book> getBooksByAuthor(@InputArgument Long authorId) {
		return bookRepository.findByAuthorId(authorId);
	}

	@DgsData(parentType = "Mutation", field = "addBook")
	public Book addBook(@InputArgument String title, @InputArgument Long authorId) {
		Author author = authorRepository.findById(authorId).orElseThrow(() -> new RuntimeException("Author not found"));
		Book book = new Book();
		book.setTitle(title);
		book.setAuthor(author);
		return bookRepository.save(book);
	}

	@DgsData(parentType = "Mutation", field = "updateBook")
	public Book updateBook(@InputArgument Long id, @InputArgument String title, @InputArgument Long authorId) {
		Optional<Book> bookOptional = bookRepository.findById(id);
		if (bookOptional.isPresent()) {
			Book book = bookOptional.get();
			book.setTitle(title);
			Author author = authorRepository.findById(authorId).orElseThrow(() -> new RuntimeException("Author not found"));
			book.setAuthor(author);
			return bookRepository.save(book);
		}
		return null;
	}

	@DgsData(parentType = "Mutation", field = "deleteBook")
	public String deleteBook(@InputArgument Long id) {
		bookRepository.deleteById(id);
		return "Book deleted successfully";
	}
}
