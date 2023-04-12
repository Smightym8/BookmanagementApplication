package at.fhv.msp.bookmanagementapplication.application.impl;

import at.fhv.msp.bookmanagementapplication.application.api.AuthorService;
import at.fhv.msp.bookmanagementapplication.application.api.exception.AuthorNotFoundException;
import at.fhv.msp.bookmanagementapplication.application.dto.author.AuthorCreateDto;
import at.fhv.msp.bookmanagementapplication.application.dto.author.AuthorDto;
import at.fhv.msp.bookmanagementapplication.application.dto.author.AuthorUpdateDto;
import at.fhv.msp.bookmanagementapplication.domain.model.Author;
import at.fhv.msp.bookmanagementapplication.domain.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorServiceImpl implements AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public List<AuthorDto> getAllAuthors() {
        return authorRepository.findAllAuthors()
                .stream()
                .map(this::authorDtoFromAuthor)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorDto getAuthorById(Long id) throws AuthorNotFoundException {
        Author author = authorRepository.findAuthorById(id).orElseThrow(
                () -> new AuthorNotFoundException("Author with id " + id + " not found")
        );

        return authorDtoFromAuthor(author);
    }

    @Override
    @Transactional
    public AuthorDto updateAuthor(Long id, AuthorUpdateDto authorUpdateDto) throws AuthorNotFoundException {
        Author author = authorRepository.findAuthorById(id).orElseThrow(
                () -> new AuthorNotFoundException("Author with id " + id + " not found")
        );

        author.setFirstName(authorUpdateDto.firstName());
        author.setLastName(authorUpdateDto.lastName());

        return authorDtoFromAuthor(author);
    }

    @Override
    @Transactional
    public Long createAuthor(AuthorCreateDto authorCreateDto) {
        Author author = authorFromAuthorCreateDto(authorCreateDto);

        authorRepository.add(author);

        return author.getAuthorId();
    }

    @Override
    @Transactional
    public AuthorDto deleteAuthor(Long id) throws AuthorNotFoundException {
        Author author = authorRepository.findAuthorById(id).orElseThrow(
                () -> new AuthorNotFoundException("Author with id " + id + " not found")
        );

        authorRepository.delete(author);

        return authorDtoFromAuthor(author);
    }

    private AuthorDto authorDtoFromAuthor(Author author) {
        return AuthorDto.builder()
                .withId(author.getAuthorId())
                .withFirstName(author.getFirstName())
                .withLastName(author.getLastName())
                .build();
    }

    private Author authorFromAuthorCreateDto(AuthorCreateDto authorCreateDto) {
        return new Author(authorCreateDto.firstName(), authorCreateDto.lastName());
    }
}
