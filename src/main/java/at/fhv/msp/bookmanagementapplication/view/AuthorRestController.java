package at.fhv.msp.bookmanagementapplication.view;

import at.fhv.msp.bookmanagementapplication.application.api.AuthorService;
import at.fhv.msp.bookmanagementapplication.application.api.exception.AuthorNotFoundException;
import at.fhv.msp.bookmanagementapplication.application.dto.author.AuthorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorRestController {
    @Autowired
    private AuthorService authorService;

    @GetMapping
    public ResponseEntity<AuthorDto[]> getAllAuthors() {
        List<AuthorDto> authorDtoList = authorService.getAllAuthors();
        AuthorDto[] authorDtoArray = new AuthorDto[authorDtoList.size()];

        return ResponseEntity.ok().body(authorDtoList.toArray(authorDtoArray));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable Long id) throws AuthorNotFoundException {
        AuthorDto authorDto = authorService.getAuthorById(id);
        return ResponseEntity.ok().body(authorDto);
    }
}
