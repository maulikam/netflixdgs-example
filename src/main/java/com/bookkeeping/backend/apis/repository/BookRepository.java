package com.bookkeeping.backend.apis.repository;

import com.bookkeeping.backend.apis.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	List<Book> findByAuthorId(Long authorId);
}
