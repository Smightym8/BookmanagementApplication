package at.fhv.msp.bookmanagementapplication.unit.application;

import at.fhv.msp.bookmanagementapplication.application.api.GenreService;
import at.fhv.msp.bookmanagementapplication.application.api.exception.GenreNotFoundException;
import at.fhv.msp.bookmanagementapplication.application.dto.genre.GenreDto;
import at.fhv.msp.bookmanagementapplication.domain.model.Genre;
import at.fhv.msp.bookmanagementapplication.domain.repository.GenreRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class GenreServiceTests {
    @Autowired
    private GenreService genreService;

    @MockBean
    private GenreRepository genreRepository;

    @Test
    void given_3GenresInRepository_when_getAllGenres_then_return_expectedDtos() {
        // given
        List<Genre> genresExpected = List.of(
                new Genre("Thriller"),
                new Genre("Novel"),
                new Genre("Science Fiction")
        );

        for(int i = 0; i < genresExpected.size(); i++) {
            genresExpected.get(i).setGenreId((long)i);
        }

        Mockito.when(genreRepository.findAllGenres()).thenReturn(genresExpected);

        // when
        List<GenreDto> genresActual = genreService.getAllGenres();

        // then
        assertEquals(genresExpected.size(), genresActual.size());

        for(int i = 0; i < genresExpected.size(); i++) {
            Genre genreExpected = genresExpected.get(i);
            GenreDto genreActual = genresActual.get(i);

            assertEquals(genreExpected.getGenreId(), genreActual.id());
            assertEquals(genreExpected.getName(), genreActual.name());
        }
    }

    @Test
    void given_genreId_when_getGenreById_then_return_expectedDto() {
        // given
        Long genreId = 100L;
        Genre genreExpected = new Genre("Thriller");
        genreExpected.setGenreId(genreId);

        Mockito.when(genreRepository.findGenreById(genreId)).thenReturn(Optional.of(genreExpected));

        // when
        GenreDto genreActual = genreService.getGenreById(genreId);

        // then
        assertEquals(genreExpected.getGenreId(), genreActual.id());
        assertEquals(genreExpected.getName(), genreActual.name());
    }

    @Test
    void given_nonExistentGenreName_when_getGenreByName_then_GenreNotFoundExceptionIsThrown() {
        // given
        Long genreId = 100L;

        Mockito.when(genreRepository.findGenreById(genreId)).thenReturn(Optional.empty());

        // when ... then
        assertThrows(GenreNotFoundException.class, () -> genreService.getGenreById(genreId));
    }
}
