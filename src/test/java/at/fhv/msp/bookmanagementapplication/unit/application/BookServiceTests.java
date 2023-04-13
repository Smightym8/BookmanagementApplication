package at.fhv.msp.bookmanagementapplication.unit.application;


import at.fhv.msp.bookmanagementapplication.application.api.BookService;
import at.fhv.msp.bookmanagementapplication.application.api.exception.AuthorNotFoundException;
import at.fhv.msp.bookmanagementapplication.application.api.exception.BookNotFoundException;
import at.fhv.msp.bookmanagementapplication.application.api.exception.IsbnAlreadyExistsException;
import at.fhv.msp.bookmanagementapplication.application.dto.book.BookCreateDto;
import at.fhv.msp.bookmanagementapplication.application.dto.book.BookDto;
import at.fhv.msp.bookmanagementapplication.application.dto.book.BookUpdateDto;
import at.fhv.msp.bookmanagementapplication.domain.model.Author;
import at.fhv.msp.bookmanagementapplication.domain.model.Book;
import at.fhv.msp.bookmanagementapplication.domain.repository.AuthorRepository;
import at.fhv.msp.bookmanagementapplication.domain.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class BookServiceTests {
    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @Test
    void given_3BooksInRepository_when_getAllBooks_then_return_expectedDtos() {
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
            assertEquals(bookExpected.getGenre(), bookActual.genre());
        }
    }

    @Test
    void given_bookInRepository_when_getBookById_then_return_expectedDto() {
        // given
        Long bookId = 1L;
        Book bookExpected =new Book(
                "1234567891234",
                "A reference book",
                LocalDate.of(2011,4,20),
                new BigDecimal("38.93"),
                "Reference book"
        );
        bookExpected.setBookId(bookId);

        Mockito.when(bookRepository.findBookById(bookId)).thenReturn(Optional.of(bookExpected));

        // when
        BookDto bookActual = bookService.getBookById(bookId);

        // then
        assertEquals(bookExpected.getBookId(), bookActual.id());
        assertEquals(bookExpected.getIsbn(), bookActual.isbn());
        assertEquals(bookExpected.getTitle(), bookActual.title());
        assertEquals(bookExpected.getPublicationDate(), bookActual.publicationDate());
        assertEquals(bookExpected.getPrice(), bookActual.price());
        assertEquals(bookExpected.getGenre(), bookActual.genre());
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
        Long bookId = 1L;
        String isbnExpected = "1234567891234";
        Book bookExpected =new Book(
                isbnExpected,
                "A reference book",
                LocalDate.of(2011,4,20),
                new BigDecimal("38.93"),
                "Reference book"
        );
        bookExpected.setBookId(bookId);

        Mockito.when(bookRepository.findBookByIsbn(isbnExpected)).thenReturn(Optional.of(bookExpected));

        // when
        BookDto bookActual = bookService.getBookByIsbn(isbnExpected);

        // then
        assertEquals(bookExpected.getBookId(), bookActual.id());
        assertEquals(bookExpected.getIsbn(), bookActual.isbn());
        assertEquals(bookExpected.getTitle(), bookActual.title());
        assertEquals(bookExpected.getPublicationDate(), bookActual.publicationDate());
        assertEquals(bookExpected.getPrice(), bookActual.price());
        assertEquals(bookExpected.getGenre(), bookActual.genre());
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

        Book book = new Book(
                "1234567891234",
                "A reference book",
                LocalDate.of(2011,4,20),
                new BigDecimal("38.93"),
                "Reference book"
        );
        book.setBookId(42L);

        BookUpdateDto bookUpdateDto = BookUpdateDto.builder()
                .withIsbn(book.getIsbn())
                .withTitle(book.getTitle())
                .withPublicationDate(book.getPublicationDate())
                .withPrice(priceExpected)
                .withGenre(book.getGenre())
                .build();

        Mockito.when(bookRepository.findBookById(book.getBookId())).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.findBookByIsbn(book.getIsbn())).thenReturn(Optional.of(book));

        // when
        BookDto bookActual = bookService.updateBook(book.getBookId(), bookUpdateDto);

        // then
        assertEquals(priceExpected, bookActual.price());
    }

    @Test
    void given_InvalidBookIdAndBookUpdateDto_when_update_then_BookNotFoundExceptionIsThrown() {
        // given
        Long bookId = 42L;

        BookUpdateDto bookUpdateDto = BookUpdateDto.builder()
                .withIsbn("1234567891234")
                .withTitle("A reference book")
                .withPublicationDate(LocalDate.of(2011,4,20))
                .withPrice(new BigDecimal("38.93"))
                .withGenre("Reference book")
                .build();

        Mockito.when(bookRepository.findBookById(bookId)).thenReturn(Optional.empty());

        // when ... then
        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(bookId, bookUpdateDto));
    }

    @Test
    void given_bookIdAndBookUpdateDto_and_AlreadyExistingIsbn_when_update_then_IsbnAlreadyExistsExceptionIsThrown() {
        // given
        Long bookId = 42L;

        Book bookToBeUpdated = new Book(
                "1234567891234",
                "A reference book",
                LocalDate.of(2011,4,20),
                new BigDecimal("38.93"),
                "Reference book"
        );

        Book otherBookWithExistingIsbn = new Book(
                "1234567899999",
                "A reference book",
                LocalDate.of(2011,4,20),
                new BigDecimal("38.93"),
                "Reference book"
        );

        BookUpdateDto bookUpdateDto = BookUpdateDto.builder()
                .withIsbn("1234567899999")
                .withTitle("A reference book")
                .withPublicationDate(LocalDate.of(2011,4,20))
                .withPrice(new BigDecimal("38.93"))
                .withGenre("Reference book")
                .build();

        Mockito.when(bookRepository.findBookById(bookId)).thenReturn(Optional.of(bookToBeUpdated));
        Mockito.when(bookRepository.findBookByIsbn(bookUpdateDto.isbn())).thenReturn(Optional.of(otherBookWithExistingIsbn));

        // when ... then
        assertThrows(IsbnAlreadyExistsException.class, () -> bookService.updateBook(bookId, bookUpdateDto));
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
                "Reference book"
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

        BookCreateDto bookCreateDto = BookCreateDto.builder()
                .withIsbn("1234567891234")
                .withTitle("A title")
                .withPublicationDate(LocalDate.now())
                .withPrice(new BigDecimal("9.99"))
                .withGenre("Horror")
                .withAuthorIds(List.of(authorId))
                .build();

        Book bookExpected = new Book(
                bookCreateDto.isbn(),
                bookCreateDto.title(),
                bookCreateDto.publicationDate(),
                bookCreateDto.price(),
                bookCreateDto.genre()
        );
        bookExpected.addAuthor(authorExpected);

        Mockito.when(bookRepository.findBookByIsbn(bookCreateDto.isbn())).thenReturn(Optional.empty());
        Mockito.when(authorRepository.findAuthorById(authorId)).thenReturn(Optional.of(authorExpected));
        Mockito.doNothing().when(bookRepository).add(bookExpected);

        // when
        bookService.createBook(bookCreateDto);

        // then
        Mockito.verify(bookRepository, Mockito.times(1)).add(bookExpected);
    }

    @Test
    void given_bookCreateDto_withExistingIsbn_when_createBook_then_IsbnAlreadyExistsExceptionIsThrown() {
        // given
        BookCreateDto bookCreateDto = BookCreateDto.builder()
                .withIsbn("1234567891234")
                .withTitle("A title")
                .withPublicationDate(LocalDate.now())
                .withPrice(new BigDecimal("9.99"))
                .withGenre("Horror")
                .withAuthorIds(List.of(1L, 2L))
                .build();

        Book book = new Book(
                bookCreateDto.isbn(),
                bookCreateDto.title(),
                bookCreateDto.publicationDate(),
                bookCreateDto.price(),
                bookCreateDto.genre()
        );

        Mockito.when(bookRepository.findBookByIsbn(bookCreateDto.isbn())).thenReturn(Optional.of(book));

        // when ... then
        assertThrows(IsbnAlreadyExistsException.class, () -> bookService.createBook(bookCreateDto));
    }

    @Test
    void given_bookCreateDtoWithNonExistentAuthorId_when_createBook_then_AuthorNotFoundExceptionIsThrown() {
        // given
        Long authorId = 42L;
        BookCreateDto bookCreateDto = BookCreateDto.builder()
                .withIsbn("1234567891234")
                .withTitle("A title")
                .withPublicationDate(LocalDate.now())
                .withPrice(new BigDecimal("9.99"))
                .withGenre("Some genre")
                .withAuthorIds(List.of(authorId))
                .build();

        Mockito.when(bookRepository.findBookByIsbn(bookCreateDto.isbn())).thenReturn(Optional.empty());
        Mockito.when(authorRepository.findAuthorById(authorId)).thenReturn(Optional.empty());

        // when ... then
        assertThrows(AuthorNotFoundException.class, () -> bookService.createBook(bookCreateDto));
    }
}
