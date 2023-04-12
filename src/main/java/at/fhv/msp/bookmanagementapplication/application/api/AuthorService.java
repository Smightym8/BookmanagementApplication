package at.fhv.msp.bookmanagementapplication.application.api;

import at.fhv.msp.bookmanagementapplication.application.api.exception.AuthorNotFoundException;
import at.fhv.msp.bookmanagementapplication.application.dto.author.AuthorCreateDto;
import at.fhv.msp.bookmanagementapplication.application.dto.author.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> getAllAuthors();
    AuthorDto getAuthorById(Long id) throws AuthorNotFoundException;
    Long createAuthor(AuthorCreateDto authorCreateDto);
    AuthorDto deleteAuthor(Long id) throws AuthorNotFoundException;
}
