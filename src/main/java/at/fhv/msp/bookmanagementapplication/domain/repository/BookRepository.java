package at.fhv.msp.bookmanagementapplication.domain.repository;

import at.fhv.msp.bookmanagementapplication.domain.model.Book;

import java.util.List;

public interface BookRepository {
    List<Book> findAllBooks();
}
