package at.fhv.msp.bookmanagementapplication.domain.repository;

import at.fhv.msp.bookmanagementapplication.domain.model.Author;

import java.util.Optional;

public interface AuthorRepository {
    Optional<Author> findAuthorById(Long id);
}
