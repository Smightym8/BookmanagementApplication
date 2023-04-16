package at.fhv.msp.bookmanagementapplication.application.api;

import at.fhv.msp.bookmanagementapplication.application.api.exception.GenreNotFoundException;
import at.fhv.msp.bookmanagementapplication.application.dto.genre.GenreDto;

import java.util.List;

public interface GenreService {
    List<GenreDto> getAllGenres();
    GenreDto getGenreById(Long id) throws GenreNotFoundException;
}
