package at.fhv.msp.bookmanagementapplication.integration.infrastructure;

import at.fhv.msp.bookmanagementapplication.domain.model.Genre;
import at.fhv.msp.bookmanagementapplication.domain.repository.GenreRepository;
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
public class GenreRepositoryTests {
    @Autowired
    private GenreRepository genreRepository;

    @Test
    void given_genres_in_repo_when_findAll_then_returnEqualGenres() {
        // given
        List<Genre> genresExpected = List.of(
            new Genre("Thriller"),
            new Genre("Novel"),
            new Genre("Science Fiction"),
            new Genre("Fantasy"),
            new Genre("Horror"),
            new Genre("Non-fiction book"),
            new Genre("Reference book"),
            new Genre("Biography")
        );

        // when
        List<Genre> genresActual = genreRepository.findAllGenres();

        // then
        assertEquals(genresExpected.size(), genresActual.size());
        for(Genre genre : genresExpected) {
            assertTrue(genresActual.contains(genre));
        }
    }

    @Test
    void given_genreInRepository_when_findGenreByName_then_returnExpectedGenre() {
        // given
        Long genreId = 100L;
        Genre genreExpected = new Genre("Thriller");

        // when
        Optional<Genre> genreOpt = genreRepository.findGenreById(genreId);

        // then
        assertTrue(genreOpt.isPresent());

        Genre genreActual = genreOpt.get();
        assertEquals(genreExpected, genreActual);
    }
}
