package com.bookkeeping.backend.apis.datafetcher;

import com.bookkeeping.backend.apis.model.Reader;
import com.bookkeeping.backend.apis.repository.ReaderRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@DgsComponent
public class ReaderDataFetcher {

	@Autowired
	private ReaderRepository readerRepository;

	@DgsData(parentType = "Query", field = "readers")
	public List<Reader> getReaders() {
		return readerRepository.findAll();
	}

	@DgsData(parentType = "Query", field = "readerById")
	public Reader getReaderById(@InputArgument Long id) {
		return readerRepository.findById(id).orElse(null);
	}

	@DgsData(parentType = "Mutation", field = "addReader")
	public Reader addReader(@InputArgument String name) {
		Reader reader = new Reader();
		reader.setName(name);
		return readerRepository.save(reader);
	}

	@DgsData(parentType = "Mutation", field = "updateReader")
	public Reader updateReader(@InputArgument Long id, @InputArgument String name) {
		Optional<Reader> readerOptional = readerRepository.findById(id);
		if (readerOptional.isPresent()) {
			Reader reader = readerOptional.get();
			reader.setName(name);
			return readerRepository.save(reader);
		}
		return null;
	}

	@DgsData(parentType = "Mutation", field = "deleteReader")
	public String deleteReader(@InputArgument Long id) {
		readerRepository.deleteById(id);
		return "Reader deleted successfully";
	}
}
