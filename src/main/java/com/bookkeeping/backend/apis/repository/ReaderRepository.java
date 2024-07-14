package com.bookkeeping.backend.apis.repository;

import com.bookkeeping.backend.apis.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {

}
