package at.fhv.msp.bookmanagementapplication.domain.repository;

import at.fhv.msp.bookmanagementapplication.domain.model.Genre;

import java.util.List;

public interface GenreRepository {
    List<Genre> findAllGenres();
}
