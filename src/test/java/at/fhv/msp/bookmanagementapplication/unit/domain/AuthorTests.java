package at.fhv.msp.bookmanagementapplication.unit.domain;

import at.fhv.msp.bookmanagementapplication.domain.model.Author;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthorTests {
    @Test
    void given_authorDetails_when_createAuthor_then_detailsEquals() {
        // given
        String firstNameExpected = "John";
        String lastNameExpected = "Doe";

        // when
        Author author = new Author(firstNameExpected, lastNameExpected);

        // then
        assertEquals(firstNameExpected, author.getFirstName());
        assertEquals(lastNameExpected, author.getLastName());
    }
}
