package at.fhv.msp.bookmanagementapplication.view;

import at.fhv.msp.bookmanagementapplication.application.api.GenreService;
import at.fhv.msp.bookmanagementapplication.application.dto.genre.GenreDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        GenreDto[] genreDtosDtoArray = new GenreDto[genretoList.size()];

        return ResponseEntity.ok().body(genretoList.toArray(genreDtosDtoArray));
    }
}
