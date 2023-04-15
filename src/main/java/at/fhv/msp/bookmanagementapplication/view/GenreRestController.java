package at.fhv.msp.bookmanagementapplication.view;

import at.fhv.msp.bookmanagementapplication.application.api.GenreService;
import at.fhv.msp.bookmanagementapplication.application.api.exception.GenreNotFoundException;
import at.fhv.msp.bookmanagementapplication.application.dto.genre.GenreDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/genres")
public class GenreRestController {
    @Autowired
    private GenreService genreService;

    @GetMapping
    public ResponseEntity<GenreDto[]> getAllGenres() {
        List<GenreDto> genretoList = genreService.getAllGenres();
        GenreDto[] genreDtoArray = new GenreDto[genretoList.size()];

        return ResponseEntity.ok().body(genretoList.toArray(genreDtoArray));
    }

    @GetMapping("{id}")
    public ResponseEntity<GenreDto> getGenreById(@PathVariable Long id) throws GenreNotFoundException {
        GenreDto genreDto = genreService.getGenreById(id);

        return ResponseEntity.ok().body(genreDto);
    }
}
