package at.fhv.msp.bookmanagementapplication.integration.infrastructure;

import at.fhv.msp.bookmanagementapplication.domain.model.Author;
import at.fhv.msp.bookmanagementapplication.domain.model.Book;
import at.fhv.msp.bookmanagementapplication.domain.repository.AuthorRepository;
import at.fhv.msp.bookmanagementapplication.domain.repository.BookRepository;
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
public class BookRepositoryTests {
    @Autowired
    private EntityManager em;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void given_3books_in_repository_when_findAllBooks_then_returnEqualBooks() {
        // given
        List<Book> booksExpected = List.of(
            new Book(
                    "1234567891234",
                    "A reference book",
                    LocalDate.of(2011,4,20),
                    new BigDecimal("38.93"),
                    "Reference book"
            ),
            new Book(
                    "9876543211234",
                    "The novel book",
                    LocalDate.of(2020,1,1),
                    new BigDecimal("58.59"),
                    "Novel"
            ),
            new Book(
                    "9874123658529",
                    "A horror book",
                    LocalDate.of(2018,11,29),
                    new BigDecimal("34.95"),
                    "Horror"
            )
        );

        Author mariaMusterfrau = new Author("Maria", "Musterfrau");
        Author maxMustermann = new Author("Max", "Mustermann");
        Author janeDoe = new Author("Jane", "Doe");
        Author johnDoe = new Author("John", "Doe");

        booksExpected.get(0).addAuthor(mariaMusterfrau);
        booksExpected.get(0).addAuthor(maxMustermann);
        booksExpected.get(1).addAuthor(janeDoe);
        booksExpected.get(2).addAuthor(johnDoe);
        booksExpected.get(2).addAuthor(johnDoe);

        // when
        List<Book> booksActual = bookRepository.findAllBooks();

        // then
        assertEquals(booksExpected.size(), booksActual.size());

        for(int i = 0; i < booksExpected.size(); i++) {
            Book bookExpected = booksExpected.get(i);
            Book bookActual = booksActual.get(i);

            assertEquals(bookExpected.getIsbn(), bookActual.getIsbn());
            assertEquals(bookExpected.getTitle(), bookActual.getTitle());
            assertEquals(bookExpected.getPublicationDate(), bookActual.getPublicationDate());
            assertEquals(bookExpected.getPrice(), bookActual.getPrice());
            assertEquals(bookExpected.getGenre(), bookActual.getGenre());

            for(Author author : bookExpected.getAuthors()) {
                assertTrue(bookActual.getAuthors().contains(author));
            }
        }
    }

    @Test
    void given_bookInRepository_when_findBookById_then_return_expectedBook() {
        // given
        Long bookIdExpected = 100L;
        Book bookExpected =  new Book(
                "1234567891234",
                "A reference book",
                LocalDate.of(2011,4,20),
                new BigDecimal("38.93"),
                "Reference book"
        );
        bookExpected.setBookId(bookIdExpected);

        Author mariaMusterfrau = new Author("Maria", "Musterfrau");
        Author maxMustermann = new Author("Max", "Mustermann");
        bookExpected.addAuthor(mariaMusterfrau);
        bookExpected.addAuthor(maxMustermann);

        // when
        Optional<Book> bookOpt = bookRepository.findBookById(bookIdExpected);

        // then
        assertTrue(bookOpt.isPresent());
        Book bookActual = bookOpt.get();

        assertEquals(bookExpected.getBookId(), bookActual.getBookId());
        assertEquals(bookExpected.getIsbn(), bookActual.getIsbn());
        assertEquals(bookExpected.getTitle(), bookActual.getTitle());
        assertEquals(bookExpected.getPublicationDate(), bookActual.getPublicationDate());
        assertEquals(bookExpected.getPrice(), bookActual.getPrice());
        assertEquals(bookExpected.getGenre(), bookActual.getGenre());

        for(Author author : bookExpected.getAuthors()) {
            assertTrue(bookActual.getAuthors().contains(author));
        }
    }

    @Test
    void given_bookInRepository_when_findBookByIsbn_then_return_expectedBook() {
        // given
        Long bookIdExpected = 100L;
        String isbnExpected = "1234567891234";
        Book bookExpected =  new Book(
                isbnExpected,
                "A reference book",
                LocalDate.of(2011,4,20),
                new BigDecimal("38.93"),
                "Reference book"
        );
        bookExpected.setBookId(bookIdExpected);

        Author mariaMusterfrau = new Author("Maria", "Musterfrau");
        Author maxMustermann = new Author("Max", "Mustermann");
        bookExpected.addAuthor(mariaMusterfrau);
        bookExpected.addAuthor(maxMustermann);

        // when
        Optional<Book> bookOpt = bookRepository.findBookByIsbn(isbnExpected);

        // then
        assertTrue(bookOpt.isPresent());
        Book bookActual = bookOpt.get();

        assertEquals(bookExpected.getBookId(), bookActual.getBookId());
        assertEquals(bookExpected.getIsbn(), bookActual.getIsbn());
        assertEquals(bookExpected.getTitle(), bookActual.getTitle());
        assertEquals(bookExpected.getPublicationDate(), bookActual.getPublicationDate());
        assertEquals(bookExpected.getPrice(), bookActual.getPrice());
        assertEquals(bookExpected.getGenre(), bookActual.getGenre());

        for(Author author : bookExpected.getAuthors()) {
            assertTrue(bookActual.getAuthors().contains(author));
        }
    }

    @Test
    void given_book_when_delete_then_bookIsDeleted() {
        // given
        Book book = bookRepository.findBookById(100L).get();

        // when
        bookRepository.delete(book);
        em.flush();

        // then
        Optional<Book> bookOpt = bookRepository.findBookById(book.getBookId());
        assertFalse(bookOpt.isPresent());
    }
    
    @Test
    void given_bookAndNewAuthor_when_add_then_bookIsSaved() {
        // given
        Author author = new Author("Author", "Author");
        String isbnExpected = "1234567895437";
        Book bookExpected =  new Book(
                isbnExpected,
                "A new book",
                LocalDate.of(1990,12,12),
                new BigDecimal("42.00"),
                "Novel"
        );

        bookExpected.addAuthor(author);

        // when
        bookRepository.add(bookExpected);
        authorRepository.add(author);
        em.flush();
        Optional<Book> bookOpt = bookRepository.findBookByIsbn(isbnExpected);

        // then
        assertTrue(bookOpt.isPresent());
        Book bookActual = bookOpt.get();
        for(Author a : bookExpected.getAuthors()) {
            assertTrue(bookActual.getAuthors().contains(a));
        }
    }

    @Test
    void given_bookAndExistingAuthor_when_add_then_bookIsSaved() {
        // given
        Author author = authorRepository.findAuthorById(100L).get();
        String isbnExpected = "1234567895437";
        Book bookExpected =  new Book(
                isbnExpected,
                "A new book",
                LocalDate.of(1990,12,12),
                new BigDecimal("42.00"),
                "Novel"
        );

        bookExpected.addAuthor(author);

        // when
        bookRepository.add(bookExpected);
        em.flush();
        Optional<Book> bookOpt = bookRepository.findBookByIsbn(isbnExpected);

        // then
        assertTrue(bookOpt.isPresent());
        Book bookActual = bookOpt.get();
        for(Author a : bookExpected.getAuthors()) {
            assertTrue(bookActual.getAuthors().contains(a));
        }
    }
}
