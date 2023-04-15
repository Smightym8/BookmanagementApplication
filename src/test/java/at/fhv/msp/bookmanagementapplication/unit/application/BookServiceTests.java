package at.fhv.msp.bookmanagementapplication.unit.application;


import at.fhv.msp.bookmanagementapplication.application.api.BookService;
import at.fhv.msp.bookmanagementapplication.application.api.exception.*;
import at.fhv.msp.bookmanagementapplication.application.dto.book.BookCreateDto;
import at.fhv.msp.bookmanagementapplication.application.dto.book.BookDto;
import at.fhv.msp.bookmanagementapplication.application.dto.book.BookUpdateDto;
import at.fhv.msp.bookmanagementapplication.domain.model.Author;
import at.fhv.msp.bookmanagementapplication.domain.model.Book;
import at.fhv.msp.bookmanagementapplication.domain.model.Genre;
import at.fhv.msp.bookmanagementapplication.domain.repository.AuthorRepository;
import at.fhv.msp.bookmanagementapplication.domain.repository.BookRepository;
import at.fhv.msp.bookmanagementapplication.domain.repository.GenreRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BookServiceTests {
    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Test
    void given_3BooksInRepository_when_getAllBooks_then_return_expectedDtos() {
        // given
        List<Book> booksExpected = List.of(
                new Book(
                        "1234567891234",
                        "A reference book",
                        LocalDate.of(2011,4,20),
                        new BigDecimal("38.93"),
                        new Genre("Reference book")
                ),
                new Book(
                        "9876543211234",
                        "The novel book",
                        LocalDate.of(2020,1,1),
                        new BigDecimal("58.59"),
                        new Genre("Novel")
                ),
                new Book(
                        "9874123658529",
                        "A horror book",
                        LocalDate.of(2018,11,29),
                        new BigDecimal("34.95"),
                        new Genre("Horror")
                )
        );

        for(int i = 0; i < booksExpected.size(); i++) {
            booksExpected.get(i).setBookId((long) i);
        }

        Mockito.when(bookRepository.findAllBooks()).thenReturn(booksExpected);

        // when
        List<BookDto> booksActual = bookService.getAllBooks();

        // then
        assertEquals(booksExpected.size(), booksActual.size());

        for(int i = 0; i < booksExpected.size(); i++) {
            Book bookExpected = booksExpected.get(i);
            BookDto bookActual = booksActual.get(i);

            assertEquals(bookExpected.getIsbn(), bookActual.isbn());
            assertEquals(bookExpected.getTitle(), bookActual.title());
            assertEquals(bookExpected.getPublicationDate(), bookActual.publicationDate());
            assertEquals(bookExpected.getPrice(), bookActual.price());
            assertEquals(bookExpected.getGenre().getName(), bookActual.genre());
        }
    }

    @Test
    void given_bookInRepository_when_getBookById_then_return_expectedDto() {
        // given
        Author author = new Author("John", "Doe");
        Long bookId = 1L;
        Book bookExpected =new Book(
                "1234567891234",
                "A reference book",
                LocalDate.of(2011,4,20),
                new BigDecimal("38.93"),
                new Genre("Reference book")
        );
        bookExpected.setBookId(bookId);
        bookExpected.addAuthor(author);

        Mockito.when(bookRepository.findBookById(bookId)).thenReturn(Optional.of(bookExpected));

        // when
        BookDto bookActual = bookService.getBookById(bookId);

        // then
        assertEquals(bookExpected.getBookId(), bookActual.id());
        assertEquals(bookExpected.getIsbn(), bookActual.isbn());
        assertEquals(bookExpected.getTitle(), bookActual.title());
        assertEquals(bookExpected.getPublicationDate(), bookActual.publicationDate());
        assertEquals(bookExpected.getPrice(), bookActual.price());
        assertEquals(bookExpected.getGenre().getName(), bookActual.genre());
        assertTrue(bookActual.authorNames().contains(author.getFirstName() + " " + author.getLastName()));
    }

    @Test
    void given_nonExistentBookId_when_getBookById_then_BookNotFoundExceptionIsReturned() {
        // given
        Long bookId = 1L;
        Mockito.when(bookRepository.findBookById(bookId)).thenReturn(Optional.empty());

        // when ... then
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(bookId));
    }

    @Test
    void given_bookInRepository_when_getBookByIsbn_then_return_expectedDto() {
        // given
        Author author = new Author("Max", "Mustermann");
        Long bookId = 1L;
        String isbnExpected = "1234567891234";
        Book bookExpected =new Book(
                isbnExpected,
                "A reference book",
                LocalDate.of(2011,4,20),
                new BigDecimal("38.93"),
                new Genre("Reference book")
        );
        bookExpected.setBookId(bookId);
        bookExpected.addAuthor(author);

        Mockito.when(bookRepository.findBookByIsbn(isbnExpected)).thenReturn(Optional.of(bookExpected));

        // when
        BookDto bookActual = bookService.getBookByIsbn(isbnExpected);

        // then
        assertEquals(bookExpected.getBookId(), bookActual.id());
        assertEquals(bookExpected.getIsbn(), bookActual.isbn());
        assertEquals(bookExpected.getTitle(), bookActual.title());
        assertEquals(bookExpected.getPublicationDate(), bookActual.publicationDate());
        assertEquals(bookExpected.getPrice(), bookActual.price());
        assertEquals(bookExpected.getGenre().getName(), bookActual.genre());
        assertTrue(bookActual.authorNames().contains(author.getFirstName() + " " + author.getLastName()));
    }

    @Test
    void given_nonExistentBookIsbn_when_getBookByIsbn_then_BookNotFoundExceptionIsReturned() {
        // given
        String isbnExpected = "1234567891234";
        Mockito.when(bookRepository.findBookByIsbn(isbnExpected)).thenReturn(Optional.empty());

        // when ... then
        assertThrows(BookNotFoundException.class, () -> bookService.getBookByIsbn(isbnExpected));
    }

    @Test
    void given_bookIdAndBookUpdateDto_when_update_then_bookIsUpdated() {
        // given
        BigDecimal priceExpected = new BigDecimal("12.99");

        Author authorBeforeUpdate = new Author("John", "Doe");
        authorBeforeUpdate.setAuthorId(42L);
        Author authorAfterUpdate = new Author("Jane", "Doe");
        authorAfterUpdate.setAuthorId(43L);

        Genre genre = new Genre("Reference book");
        genre.setGenreId(100L);
        Book book = new Book(
                "1234567891234",
                "A reference book",
                LocalDate.of(2011,4,20),
                new BigDecimal("38.93"),
                genre
        );
        book.setBookId(42L);
        book.addAuthor(authorBeforeUpdate);

        BookUpdateDto bookUpdateDto = BookUpdateDto.builder()
                .withIsbn(book.getIsbn())
                .withTitle(book.getTitle())
                .withPublicationDate(book.getPublicationDate())
                .withPrice(priceExpected)
                .withGenreId(book.getGenre().getGenreId())
                .withAuthorIds(List.of(authorAfterUpdate.getAuthorId()))
                .build();

        Mockito.when(bookRepository.findBookById(book.getBookId())).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.findBookByIsbn(book.getIsbn())).thenReturn(Optional.of(book));
        Mockito.when(genreRepository.findGenreById(genre.getGenreId())).thenReturn(Optional.of(genre));
        Mockito.when(authorRepository.findAuthorById(authorAfterUpdate.getAuthorId())).thenReturn(Optional.of(authorAfterUpdate));

        // when
        BookDto bookActual = bookService.updateBook(book.getBookId(), bookUpdateDto);

        // then
        assertEquals(priceExpected, bookActual.price());
        assertTrue(bookActual.authorNames().contains(authorAfterUpdate.getFirstName() + " " + authorAfterUpdate.getLastName()));
        assertFalse(bookActual.authorNames().contains(authorBeforeUpdate.getFirstName() + " " + authorBeforeUpdate.getLastName()));
    }

    @Test
    void given_InvalidBookIdAndBookUpdateDto_when_update_then_BookNotFoundExceptionIsThrown() {
        // given
        Long bookId = 42L;

        Author author = new Author("John", "Doe");
        author.setAuthorId(42L);
        BookUpdateDto bookUpdateDto = BookUpdateDto.builder()
                .withIsbn("1234567891234")
                .withTitle("A reference book")
                .withPublicationDate(LocalDate.of(2011,4,20))
                .withPrice(new BigDecimal("38.93"))
                .withGenreId(100L)
                .withAuthorIds(List.of(author.getAuthorId()))
                .build();

        Mockito.when(bookRepository.findBookById(bookId)).thenReturn(Optional.empty());

        // when ... then
        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(bookId, bookUpdateDto));
    }

    @Test
    void given_bookIdAndBookUpdateDto_and_AlreadyExistingIsbn_when_update_then_IsbnAlreadyExistsExceptionIsThrown() {
        // given
        Long bookId = 42L;

        Genre genre = new Genre("Reference book");
        genre.setGenreId(100L);
        Book bookToBeUpdated = new Book(
                "1234567891234",
                "A reference book",
                LocalDate.of(2011,4,20),
                new BigDecimal("38.93"),
                genre
        );

        Book otherBookWithExistingIsbn = new Book(
                "1234567899999",
                "A reference book",
                LocalDate.of(2011,4,20),
                new BigDecimal("38.93"),
                new Genre("Reference book")
        );

        Author author = new Author("John", "Doe");
        author.setAuthorId(42L);
        BookUpdateDto bookUpdateDto = BookUpdateDto.builder()
                .withIsbn("1234567899999")
                .withTitle(bookToBeUpdated.getIsbn())
                .withPublicationDate(bookToBeUpdated.getPublicationDate())
                .withPrice(bookToBeUpdated.getPrice())
                .withGenreId(bookToBeUpdated.getGenre().getGenreId())
                .withAuthorIds(List.of(author.getAuthorId()))
                .build();

        Mockito.when(bookRepository.findBookById(bookId)).thenReturn(Optional.of(bookToBeUpdated));
        Mockito.when(bookRepository.findBookByIsbn(bookUpdateDto.isbn())).thenReturn(Optional.of(otherBookWithExistingIsbn));

        // when ... then
        assertThrows(IsbnAlreadyExistsException.class, () -> bookService.updateBook(bookId, bookUpdateDto));
    }

    @Test
    void given_bookIdAndInvalidBookUpdateDto_when_updateBook_then_InvalidBookUpdateExceptionIsThrown() {
        // given
        Long bookId = 42L;
        BookUpdateDto bookUpdateDto = BookUpdateDto.builder()
                .withIsbn("1234567899999")
                .withTitle("A reference book")
                .withPublicationDate(LocalDate.of(2011,4,20))
                .withPrice(new BigDecimal("38.93"))
                .withGenreId(100L)
                .withAuthorIds(Collections.emptyList())
                .build();

        // when ... then
        assertThrows(InvalidBookUpdateException.class, () -> bookService.updateBook(bookId, bookUpdateDto));
    }

    @Test
    void given_bookId_when_deleteBook_then_deleteIsCalled() {
        // given
        Long bookId = 42L;
        Book book =new Book(
                "1234567891234",
                "A reference book",
                LocalDate.of(2011,4,20),
                new BigDecimal("38.93"),
                new Genre("Reference book")
        );
        book.setBookId(bookId);

        Mockito.when(bookRepository.findBookById(bookId)).thenReturn(Optional.of(book));
        Mockito.doNothing().when(bookRepository).delete(book);

        // when
        bookService.deleteBook(bookId);

        // then
        Mockito.verify(bookRepository, Mockito.times(1)).delete(book);
    }

    @Test
    void given_nonExistentBookId_when_deleteBook_then_BookNotFoundExceptionIsThrown() {
        // given
        Long bookId = 42L;

        Mockito.when(bookRepository.findBookById(bookId)).thenReturn(Optional.empty());

        // when ... then
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(bookId));
    }

    @Test
    void given_bookCreateDto_when_createBook_then_addIsCalled() {
        // given
        Long authorId = 42L;
        Author authorExpected = new Author("John", "Doe");
        Genre genre = new Genre("Horror");
        genre.setGenreId(100L);

        BookCreateDto bookCreateDto = BookCreateDto.builder()
                .withIsbn("1234567891234")
                .withTitle("A title")
                .withPublicationDate(LocalDate.now())
                .withPrice(new BigDecimal("9.99"))
                .withGenreId(genre.getGenreId())
                .withAuthorIds(List.of(authorId))
                .build();

        Book bookExpected = new Book(
                bookCreateDto.isbn(),
                bookCreateDto.title(),
                bookCreateDto.publicationDate(),
                bookCreateDto.price(),
                genre
        );
        bookExpected.addAuthor(authorExpected);

        Mockito.when(bookRepository.findBookByIsbn(bookCreateDto.isbn())).thenReturn(Optional.empty());
        Mockito.when(authorRepository.findAuthorById(authorId)).thenReturn(Optional.of(authorExpected));
        Mockito.when(genreRepository.findGenreById(genre.getGenreId())).thenReturn(Optional.of(genre));
        Mockito.doNothing().when(bookRepository).add(bookExpected);

        // when
        bookService.createBook(bookCreateDto);

        // then
        Mockito.verify(bookRepository, Mockito.times(1)).add(bookExpected);
    }

    @Test
    void given_bookCreateDto_withExistingIsbn_when_createBook_then_IsbnAlreadyExistsExceptionIsThrown() {
        // given
        Genre genre = new Genre("Horror");
        genre.setGenreId(100L);
        BookCreateDto bookCreateDto = BookCreateDto.builder()
                .withIsbn("1234567891234")
                .withTitle("A title")
                .withPublicationDate(LocalDate.now())
                .withPrice(new BigDecimal("9.99"))
                .withGenreId(genre.getGenreId())
                .withAuthorIds(List.of(1L, 2L))
                .build();

        Book book = new Book(
                bookCreateDto.isbn(),
                bookCreateDto.title(),
                bookCreateDto.publicationDate(),
                bookCreateDto.price(),
                genre
        );

        Mockito.when(bookRepository.findBookByIsbn(bookCreateDto.isbn())).thenReturn(Optional.of(book));

        // when ... then
        assertThrows(IsbnAlreadyExistsException.class, () -> bookService.createBook(bookCreateDto));
    }

    @Test
    void given_bookCreateDtoWithNonExistentAuthorId_when_createBook_then_AuthorNotFoundExceptionIsThrown() {
        // given
        Genre genre = new Genre("Horror");
        genre.setGenreId(100L);
        Long authorId = 42L;
        BookCreateDto bookCreateDto = BookCreateDto.builder()
                .withIsbn("1234567891234")
                .withTitle("A title")
                .withPublicationDate(LocalDate.now())
                .withPrice(new BigDecimal("9.99"))
                .withGenreId(100L)
                .withAuthorIds(List.of(authorId))
                .build();

        Mockito.when(bookRepository.findBookByIsbn(bookCreateDto.isbn())).thenReturn(Optional.empty());
        Mockito.when(genreRepository.findGenreById(genre.getGenreId())).thenReturn(Optional.of(genre));
        Mockito.when(authorRepository.findAuthorById(authorId)).thenReturn(Optional.empty());

        // when ... then
        assertThrows(AuthorNotFoundException.class, () -> bookService.createBook(bookCreateDto));
    }

    @Test
    void given_invalidBookCreateDto_when_createBook_then_InvalidBookCreationExceptionIsThrown() {
        // given
        BookCreateDto bookCreateDto = BookCreateDto.builder()
                .withIsbn("1234567891234")
                .withTitle("A title")
                .withPublicationDate(LocalDate.now())
                .withPrice(new BigDecimal("9.99"))
                .withGenreId(100L)
                .withAuthorIds(Collections.emptyList())
                .build();

        // when ... then
        assertThrows(InvalidBookCreationException.class, () -> bookService.createBook(bookCreateDto));
    }
}
