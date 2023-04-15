package at.fhv.msp.bookmanagementapplication.unit.domain;

import at.fhv.msp.bookmanagementapplication.domain.model.Genre;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenreTests {
    @Test
    void given_genreDetails_when_createGenre_then_detailsEquals() {
        // given
        String nameExpected = "Fantasy";

        // when
        Genre genre = new Genre(nameExpected);

        // then
        assertEquals(nameExpected, genre.getName());
    }
}
