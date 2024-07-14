package com.bookkeeping.backend.apis.datafetcher;

import com.bookkeeping.backend.apis.model.Reader;
import com.bookkeeping.backend.apis.repository.ReaderRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DgsComponent
public class ReaderDataFetcher {

	@Autowired
	private ReaderRepository readerRepository;

	@DgsQuery
	public List<Reader> readers() {
		return readerRepository.findAll();
	}

	@DgsMutation
	public Reader addReader(String name) {
		Reader reader = new Reader();
		reader.setName(name);
		return readerRepository.save(reader);
	}
}
