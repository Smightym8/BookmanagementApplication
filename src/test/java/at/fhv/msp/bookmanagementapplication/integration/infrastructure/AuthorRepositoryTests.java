package at.fhv.msp.bookmanagementapplication.integration.infrastructure;

import at.fhv.msp.bookmanagementapplication.domain.model.Author;
import at.fhv.msp.bookmanagementapplication.domain.model.Book;
import at.fhv.msp.bookmanagementapplication.domain.model.Genre;
import at.fhv.msp.bookmanagementapplication.domain.repository.AuthorRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

        Book referenceBook = new Book(
            "1234567891234",
            "A reference book",
            LocalDate.of(2011,4,20),
            new BigDecimal("38.93"),
            new Genre("Reference book")
        );
        Book novelBook = new Book(
            "9876543211234",
            "The novel book",
            LocalDate.of(2020,1,1),
            new BigDecimal("58.59"),
            new Genre("Novel")
        );
        Book horrorBook = new Book(
            "9874123658529",
            "A horror book",
            LocalDate.of(2018,11,29),
            new BigDecimal("34.95"),
            new Genre("Horror")
        );

        referenceBook.addAuthor(authorsExpected.get(2));
        referenceBook.addAuthor(authorsExpected.get(3));
        novelBook.addAuthor(authorsExpected.get(1));
        horrorBook.addAuthor(authorsExpected.get(0));
        horrorBook.addAuthor(authorsExpected.get(1));

        // when
        List<Author> authorsActual = authorRepository.findAllAuthors();

        // then
        assertEquals(authorsExpected.size(), authorsActual.size());

        for(int i = 0; i < authorsExpected.size(); i++) {
            Author authorExpected = authorsExpected.get(i);
            Author authorActual = authorsActual.get(i);

            assertEquals(authorExpected.getFirstName(), authorActual.getFirstName());
            assertEquals(authorExpected.getLastName(), authorActual.getLastName());

            for(Book book : authorExpected.getBooks()) {
                assertTrue(authorActual.getBooks().contains(book));
            }
        }
    }

    @Test
    void given_authorInRepository_when_findAuthorById_then_return_expectedAuthor() {
        // given
        Long authorId = 100L;
        String firstNameExpected = "John";
        String lastNameExpected = "Doe";

        Book horrorBook = new Book(
                "9874123658529",
                "A horror book",
                LocalDate.of(2018,11,29),
                new BigDecimal("34.95"),
                new Genre("Horror")
        );

        // when
        Optional<Author> authorOpt = authorRepository.findAuthorById(authorId);

        // then
        assertTrue(authorOpt.isPresent());
        Author authorActual = authorOpt.get();

        assertEquals(firstNameExpected, authorActual.getFirstName());
        assertEquals(lastNameExpected, authorActual.getLastName());
        assertTrue(authorActual.getBooks().contains(horrorBook));
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

    @Test
    void given_author_when_delete_then_authorIsDeleted() {
        // given
        Long authorId = 100L;
        Author author = authorRepository.findAuthorById(authorId).get();

        // when
        authorRepository.delete(author);

        // then
        Optional<Author> authorOpt = authorRepository.findAuthorById(authorId);
        assertFalse(authorOpt.isPresent());
    }
}
