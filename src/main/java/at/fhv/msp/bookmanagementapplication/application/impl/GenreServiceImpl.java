package at.fhv.msp.bookmanagementapplication.application.impl;

import at.fhv.msp.bookmanagementapplication.application.api.GenreService;
import at.fhv.msp.bookmanagementapplication.application.dto.genre.GenreDto;
import at.fhv.msp.bookmanagementapplication.domain.model.Genre;
import at.fhv.msp.bookmanagementapplication.domain.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenreServiceImpl implements GenreService {
    @Autowired
    private GenreRepository genreRepository;

    @Override
    public List<GenreDto> getAllGenres() {
        return genreRepository.findAllGenres()
                .stream()
                .map(this::genreDtoFromGenre)
                .toList();
    }

    private GenreDto genreDtoFromGenre(Genre genre) {
        return GenreDto.builder()
                .withId(genre.getGenreId())
                .withName(genre.getName())
                .build();
    }
}
