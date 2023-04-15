package at.fhv.msp.bookmanagementapplication.application.impl;

import at.fhv.msp.bookmanagementapplication.application.api.AuthorService;
import at.fhv.msp.bookmanagementapplication.application.api.exception.AuthorNotFoundException;
import at.fhv.msp.bookmanagementapplication.application.api.exception.BookNotFoundException;
import at.fhv.msp.bookmanagementapplication.application.api.exception.InvalidAuthorDeletionException;
import at.fhv.msp.bookmanagementapplication.application.api.exception.InvalidAuthorUpdateException;
import at.fhv.msp.bookmanagementapplication.application.dto.author.AuthorCreateDto;
import at.fhv.msp.bookmanagementapplication.application.dto.author.AuthorDto;
import at.fhv.msp.bookmanagementapplication.application.dto.author.AuthorUpdateDto;
import at.fhv.msp.bookmanagementapplication.domain.model.Author;
import at.fhv.msp.bookmanagementapplication.domain.model.Book;
import at.fhv.msp.bookmanagementapplication.domain.repository.AuthorRepository;
import at.fhv.msp.bookmanagementapplication.domain.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorServiceImpl implements AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

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
    public AuthorDto updateAuthor(Long id, AuthorUpdateDto authorUpdateDto) throws AuthorNotFoundException, InvalidAuthorUpdateException {
        Author author = authorRepository.findAuthorById(id).orElseThrow(
                () -> new AuthorNotFoundException("Author with id " + id + " not found")
        );

        // Update author
        author.setFirstName(authorUpdateDto.firstName());
        author.setLastName(authorUpdateDto.lastName());

        // Remove books that are not in the updated list
        List<Book> booksToRemove = author.getBooks().stream()
                .filter(book -> !authorUpdateDto.bookIds().contains(book.getBookId()))
                .toList();

        booksToRemove.forEach(book -> {
            if(book.getAuthors().size() == 1) {
                throw new InvalidAuthorUpdateException("Could not remove book " + book.getTitle() +
                        " from author " + author.getFirstName() + " " + author.getLastName());
            } else {
                book.removeAuthor(author);
            }
        });

        // Add books that are in updated list but not in current books list of author
        authorUpdateDto.bookIds().stream()
                .map(bookId -> bookRepository.findBookById(bookId).orElseThrow(
                        () -> new BookNotFoundException("Book with id " + bookId + " not found")
                ))
                .filter(book -> !author.getBooks().contains(book))
                .toList()
                .forEach(book -> book.addAuthor(author));

        return authorDtoFromAuthor(author);
    }

    @Override
    @Transactional
    public Long createAuthor(AuthorCreateDto authorCreateDto) throws BookNotFoundException {
        Author author = authorFromAuthorCreateDto(authorCreateDto);

        // Add books to author
        authorCreateDto.bookIds()
                .stream()
                .map(bookId -> bookRepository.findBookById(bookId).orElseThrow(
                        () -> new BookNotFoundException("Book with id " + bookId + " not found")
                ))
                .toList()
                .forEach(book -> book.addAuthor(author));

        authorRepository.add(author);

        return author.getAuthorId();
    }

    @Override
    @Transactional
    public AuthorDto deleteAuthor(Long id) throws AuthorNotFoundException, InvalidAuthorDeletionException {
        Author author = authorRepository.findAuthorById(id).orElseThrow(
                () -> new AuthorNotFoundException("Author with id " + id + " not found")
        );

        // Check that a book is not without authors when this author is deleted
        author.getBooks().forEach(book -> {
            if(book.getAuthors().size() == 1) {
                throw new InvalidAuthorDeletionException("Could not delete author with id" + id +
                        " because it is the only author for book " + book.getTitle());
            }
        });

        authorRepository.delete(author);

        return authorDtoFromAuthor(author);
    }

    private AuthorDto authorDtoFromAuthor(Author author) {
        return AuthorDto.builder()
                .withId(author.getAuthorId())
                .withFirstName(author.getFirstName())
                .withLastName(author.getLastName())
                .withBookNames(author.getBooks()
                        .stream()
                        .map(Book::getTitle)
                        .toList()
                )
                .build();
    }

    private Author authorFromAuthorCreateDto(AuthorCreateDto authorCreateDto) {
        return new Author(authorCreateDto.firstName(), authorCreateDto.lastName());
    }
}
