package at.fhv.msp.bookmanagementapplication.integration.infrastructure;

import at.fhv.msp.bookmanagementapplication.domain.model.Book;
import at.fhv.msp.bookmanagementapplication.domain.repository.BookRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class BookRepositoryTests {
    @Autowired
    private EntityManager em;

    @Autowired
    private BookRepository bookRepository;

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
        }
    }
}
