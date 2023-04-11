package at.fhv.msp.bookmanagementapplication.application.impl;

import at.fhv.msp.bookmanagementapplication.application.api.AuthorService;
import at.fhv.msp.bookmanagementapplication.application.api.exception.AuthorNotFoundException;
import at.fhv.msp.bookmanagementapplication.application.dto.author.AuthorDto;
import at.fhv.msp.bookmanagementapplication.domain.model.Author;
import at.fhv.msp.bookmanagementapplication.domain.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorServiceImpl implements AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public AuthorDto getAuthorById(Long id) throws AuthorNotFoundException {
        Author author = authorRepository.findAuthorById(id).orElseThrow(
                () -> new AuthorNotFoundException("Author with id " + id + " not found")
        );

        return authorDtoFromAuthor(author);
    }

    private AuthorDto authorDtoFromAuthor(Author author) {
        return AuthorDto.builder()
                .withId(author.getAuthorId())
                .withFirstName(author.getFirstName())
                .withLastName(author.getLastName())
                .build();
    }
}
