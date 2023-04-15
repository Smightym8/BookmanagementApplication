package at.fhv.msp.bookmanagementapplication.application.api;

import at.fhv.msp.bookmanagementapplication.application.api.exception.AuthorNotFoundException;
import at.fhv.msp.bookmanagementapplication.application.api.exception.BookNotFoundException;
import at.fhv.msp.bookmanagementapplication.application.api.exception.InvalidAuthorDeletionException;
import at.fhv.msp.bookmanagementapplication.application.dto.author.AuthorCreateDto;
import at.fhv.msp.bookmanagementapplication.application.dto.author.AuthorDto;
import at.fhv.msp.bookmanagementapplication.application.dto.author.AuthorUpdateDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> getAllAuthors();
    AuthorDto getAuthorById(Long id) throws AuthorNotFoundException;
    AuthorDto updateAuthor(Long id, AuthorUpdateDto authorUpdateDto) throws AuthorNotFoundException;
    AuthorDto deleteAuthor(Long id) throws AuthorNotFoundException, InvalidAuthorDeletionException;
    Long createAuthor(AuthorCreateDto authorCreateDto) throws BookNotFoundException;
}
