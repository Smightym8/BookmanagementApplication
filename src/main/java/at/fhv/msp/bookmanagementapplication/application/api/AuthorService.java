package at.fhv.msp.bookmanagementapplication.application.api;

import at.fhv.msp.bookmanagementapplication.application.api.exception.AuthorNotFoundException;
import at.fhv.msp.bookmanagementapplication.application.dto.author.AuthorDto;

public interface AuthorService {
    AuthorDto getAuthorById(Long id) throws AuthorNotFoundException;
}
