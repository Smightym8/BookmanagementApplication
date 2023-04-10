package at.fhv.msp.bookmanagementapplication.unit.application;


import at.fhv.msp.bookmanagementapplication.application.api.BookService;
import at.fhv.msp.bookmanagementapplication.application.dto.book.BookDto;
import at.fhv.msp.bookmanagementapplication.domain.model.Book;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class BookServiceTests {
    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

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
}
