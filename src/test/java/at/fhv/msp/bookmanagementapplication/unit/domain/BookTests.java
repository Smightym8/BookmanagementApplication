package at.fhv.msp.bookmanagementapplication.unit.domain;

import at.fhv.msp.bookmanagementapplication.domain.model.Book;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookTests {
    @Test
    void given_bookDetails_when_createBook_then_detailsEquals() {
        // given
        String isbnExpected = "978-0321774514";
        String titleExpected = "A Book";
        LocalDate publicationDateExpected = LocalDate.now();
        BigDecimal priceExpected = new BigDecimal("10");
        String genreExpected = "A genre";

        // when
        Book book = new Book(isbnExpected, titleExpected, publicationDateExpected, priceExpected, genreExpected);

        // then
        assertEquals(isbnExpected, book.getIsbn());
        assertEquals(titleExpected, book.getTitle());
        assertEquals(publicationDateExpected, book.getPublicationDate());
        assertEquals(priceExpected, book.getPrice());
        assertEquals(genreExpected, book.getGenre());
        assertEquals(0, book.getAuthors().size());
    }
}
