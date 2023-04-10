package at.fhv.msp.bookmanagementapplication.domain.repository;

import at.fhv.msp.bookmanagementapplication.domain.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAllBooks();
    Optional<Book> findBookById(Long id);
    Optional<Book> findBookByIsbn(String isbn);
    void delete(Book book);
}
