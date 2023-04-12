package at.fhv.msp.bookmanagementapplication.view;

import at.fhv.msp.bookmanagementapplication.application.api.AuthorService;
import at.fhv.msp.bookmanagementapplication.application.api.exception.AuthorNotFoundException;
import at.fhv.msp.bookmanagementapplication.application.dto.author.AuthorCreateDto;
import at.fhv.msp.bookmanagementapplication.application.dto.author.AuthorDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @PostMapping
    public ResponseEntity<Void> createAuthor(@RequestBody AuthorCreateDto authorCreateDto, HttpServletRequest request) {
        Long createdAuthorId = authorService.createAuthor(authorCreateDto);

        URI location = ServletUriComponentsBuilder.fromRequestUri(request).path("/{id}")
                .buildAndExpand(createdAuthorId).toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<AuthorDto> deleteAuthor(@PathVariable Long id) throws AuthorNotFoundException {
        AuthorDto deletedAuthor = authorService.deleteAuthor(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(deletedAuthor);
    }
}
