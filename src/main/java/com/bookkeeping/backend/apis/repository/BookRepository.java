package com.bookkeeping.backend.apis.repository;

import com.bookkeeping.backend.apis.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

}
