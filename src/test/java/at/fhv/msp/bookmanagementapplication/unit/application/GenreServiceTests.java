package at.fhv.msp.bookmanagementapplication.unit.application;

import at.fhv.msp.bookmanagementapplication.application.api.GenreService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

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

            assertEquals(genreExpected.getName(), genreActual.name());
        }
    }
}
