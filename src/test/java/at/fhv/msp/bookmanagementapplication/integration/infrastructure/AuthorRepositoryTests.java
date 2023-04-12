package at.fhv.msp.bookmanagementapplication.integration.infrastructure;

import at.fhv.msp.bookmanagementapplication.domain.model.Author;
import at.fhv.msp.bookmanagementapplication.domain.repository.AuthorRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class AuthorRepositoryTests {
    @Autowired
    private EntityManager em;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void given_4authorsInRepository_when_findAllAuthors_then_return_expectedAuthors() {
        // given
        List<Author> authorsExpected = List.of(
            new Author("John", "Doe"),
            new Author("Jane", "Doe"),
            new Author("Max", "Mustermann"),
            new Author("Maria", "Musterfrau")
        );

        // when
        List<Author> authorsActual = authorRepository.findAllAuthors();

        // then
        assertEquals(authorsExpected.size(), authorsActual.size());

        for(Author author : authorsExpected) {
            assertTrue(authorsActual.contains(author));
        }
    }

    @Test
    void given_authorInRepository_when_findAuthorById_then_return_expectedAuthor() {
        // given
        Long authorId = 100L;
        String firstNameExpected = "John";
        String lastNameExpected = "Doe";

        // when
        Optional<Author> authorOpt = authorRepository.findAuthorById(authorId);

        // then
        assertTrue(authorOpt.isPresent());
        Author authorActual = authorOpt.get();

        assertEquals(firstNameExpected, authorActual.getFirstName());
        assertEquals(lastNameExpected, authorActual.getLastName());
    }

    @Test
    void given_author_when_add_then_authorIsSaved() {
        // given
        Author authorExpected = new Author("Test", "Author");

        // when
        authorRepository.add(authorExpected);
        em.flush();

        // then
        List<Author> authors = authorRepository.findAllAuthors();
        assertTrue(authors.contains(authorExpected));
    }
}
