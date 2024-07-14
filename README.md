
# Netflix DGS Spring Boot Example

This project demonstrates how to use Netflix DGS (Domain Graph Service) with Spring Boot to build a GraphQL API. The project includes a simple library management system with entities such as `Book`, `Author`, and `Reader`.

## Table of Contents

- [Netflix DGS Spring Boot Example](#netflix-dgs-spring-boot-example)
  - [Table of Contents](#table-of-contents)
  - [Prerequisites](#prerequisites)
  - [Getting Started](#getting-started)
    - [Clone the Repository](#clone-the-repository)
    - [Build the Project](#build-the-project)
    - [Run the Application](#run-the-application)
  - [Project Structure](#project-structure)
  - [GraphQL Schema](#graphql-schema)
  - [Data Fetchers](#data-fetchers)
    - [AuthorDataFetcher](#authordatafetcher)
    - [BookDataFetcher](#bookdatafetcher)
    - [ReaderDataFetcher](#readerdatafetcher)
  - [Testing the API](#testing-the-api)
  - [License](#license)

## Prerequisites

- JDK 21
- Docker (for PostgreSQL)
- Gradle

## Getting Started

### Clone the Repository

```sh
git clone https://github.com/maulikam/netflix-dgs-example.git
cd netflix-dgs-spring-boot-example
```

### Build the Project

Make sure you have Gradle installed, then build the project:

```sh
./gradlew clean build
```

### Run the Application

To start the PostgreSQL database using Docker, run:

```sh
docker-compose up -d
```

Then run the Spring Boot application:

```sh
./gradlew bootRun
```

The application will start and run on `http://localhost:9191`.

## Project Structure

```
netflix-dgs-spring-boot-example
│
├── build.gradle
├── docker-compose.yml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── bookkeeping
│   │   │           └── backend
│   │   │               ├── ApisApplication.java
│   │   │               ├── datafetcher
│   │   │               │   ├── AuthorDataFetcher.java
│   │   │               │   ├── BookDataFetcher.java
│   │   │               │   └── ReaderDataFetcher.java
│   │   │               ├── model
│   │   │               │   ├── Author.java
│   │   │               │   ├── Book.java
│   │   │               │   └── Reader.java
│   │   │               └── repository
│   │   │                   ├── AuthorRepository.java
│   │   │                   ├── BookRepository.java
│   │   │                   └── ReaderRepository.java
│   │   └── resources
│   │       └── graphql-client
│   │           └── schema.graphqls
│   └── test
│       └── java
│           └── com
│               └── bookkeeping
│                   └── backend
│                       └── ApisApplicationTests.java
└── README.md
```

## GraphQL Schema

The GraphQL schema is defined in `src/main/resources/graphql-client/schema.graphqls`:

```graphql
type Query {
    books: [Book]
    authors: [Author]
    readers: [Reader]
}

type Mutation {
    addBook(title: String, authorId: ID): Book
    addAuthor(name: String): Author
    addReader(name: String): Reader
}

type Book {
    id: ID
    title: String
    author: Author
}

type Author {
    id: ID
    name: String
}

type Reader {
    id: ID
    name: String
}
```

## Data Fetchers

### AuthorDataFetcher

Handles queries and mutations related to authors.

```java
package com.bookkeeping.backend.apis.datafetcher;

import com.bookkeeping.backend.apis.model.Author;
import com.bookkeeping.backend.apis.repository.AuthorRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DgsComponent
public class AuthorDataFetcher {

    @Autowired
    private AuthorRepository authorRepository;

    @DgsData(parentType = "Query", field = "authors")
    public List<Author> getAuthors() {
        return authorRepository.findAll();
    }

    @DgsData(parentType = "Mutation", field = "addAuthor")
    public Author addAuthor(@InputArgument String name) {
        Author author = new Author();
        author.setName(name);
        return authorRepository.save(author);
    }
}
```

### BookDataFetcher

Handles queries and mutations related to books.

```java
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

    @DgsData(parentType = "Mutation", field = "addBook")
    public Book addBook(@InputArgument String title, @InputArgument Long authorId) {
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new RuntimeException("Author not found"));
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        return bookRepository.save(book);
    }
}
```

### ReaderDataFetcher

Handles queries and mutations related to readers.

```java
package com.bookkeeping.backend.apis.datafetcher;

import com.bookkeeping.backend.apis.model.Reader;
import com.bookkeeping.backend.apis.repository.ReaderRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DgsComponent
public class ReaderDataFetcher {

    @Autowired
    private ReaderRepository readerRepository;

    @DgsData(parentType = "Query", field = "readers")
    public List<Reader> getReaders() {
        return readerRepository.findAll();
    }

    @DgsData(parentType = "Mutation", field = "addReader")
    public Reader addReader(@InputArgument String name) {
        Reader reader = new Reader();
        reader.setName(name);
        return readerRepository.save(reader);
    }
}
```

## Testing the API

Visit `http://localhost:9191/graphiql` to interact with the GraphQL API. You can use the following queries and mutations for testing:

### Queries

```graphql
{
  books {
    id
    title
    author {
      id
      name
    }
  }
  authors {
    id
    name
  }
  readers {
    id
    name
  }
}
```

### Mutations

```graphql
mutation {
  addBook(title: "New Book", authorId: 1) {
    id
    title
  }
  addAuthor(name: "New Author") {
    id
    name
  }
  addReader(name: "New Reader") {
    id
    name
  }
}
```

## License

This project is licensed under the MIT License.
