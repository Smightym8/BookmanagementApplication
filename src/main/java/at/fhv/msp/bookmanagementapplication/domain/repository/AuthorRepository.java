package at.fhv.msp.bookmanagementapplication.domain.repository;

import at.fhv.msp.bookmanagementapplication.domain.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    List<Author> findAllAuthors();
    Optional<Author> findAuthorById(Long id);
    void add(Author author);
    void delete(Author author);
}
