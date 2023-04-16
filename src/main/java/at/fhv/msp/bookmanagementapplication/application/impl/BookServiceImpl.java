package at.fhv.msp.bookmanagementapplication.application.impl;


import at.fhv.msp.bookmanagementapplication.application.api.BookService;
import at.fhv.msp.bookmanagementapplication.application.api.exception.*;
import at.fhv.msp.bookmanagementapplication.application.dto.book.BookCreateDto;
import at.fhv.msp.bookmanagementapplication.application.dto.book.BookDto;
import at.fhv.msp.bookmanagementapplication.application.dto.book.BookUpdateDto;
import at.fhv.msp.bookmanagementapplication.domain.model.Author;
import at.fhv.msp.bookmanagementapplication.domain.model.Book;
import at.fhv.msp.bookmanagementapplication.domain.model.Genre;
import at.fhv.msp.bookmanagementapplication.domain.repository.AuthorRepository;
import at.fhv.msp.bookmanagementapplication.domain.repository.BookRepository;
import at.fhv.msp.bookmanagementapplication.domain.repository.GenreRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Override
    public List<BookDto> getAllBooks() {
        return bookRepository.findAllBooks()
                .stream()
                .map(this::bookDtoFromBook)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto getBookById(Long id) throws BookNotFoundException {
        Book book = bookRepository.findBookById(id).orElseThrow(
                () -> new BookNotFoundException("Book with id " + id + " not found")
        );

        return bookDtoFromBook(book);
    }

    @Override
    public BookDto getBookByIsbn(String isbn) throws BookNotFoundException {
        Book book = bookRepository.findBookByIsbn(isbn).orElseThrow(
                () -> new BookNotFoundException("Book with isbn " + isbn + " not found")
        );

        return bookDtoFromBook(book);
    }

    @Override
    @Transactional
    public BookDto updateBook(Long id, BookUpdateDto bookUpdateDto) throws BookNotFoundException,
            IsbnAlreadyExistsException, InvalidBookUpdateException, GenreNotFoundException {
        if(bookUpdateDto.authorIds().size() == 0) {
            throw new InvalidBookUpdateException("Can not create book without author");
        }

        Book bookToBeUpdated = bookRepository.findBookById(id).orElseThrow(
                () -> new BookNotFoundException("Book with id " + id + " not found")
        );

        // Check if there is already a book with provided isbn and it is not the book to be updated
        Optional<Book> bookForProvidedIsbn = bookRepository.findBookByIsbn(bookUpdateDto.isbn());

        if(bookForProvidedIsbn.isPresent() && !Objects.equals(bookToBeUpdated.getIsbn(), bookUpdateDto.isbn())) {
            throw new IsbnAlreadyExistsException("There is already a book with isbn " + bookUpdateDto.isbn());
        }

        // Update book
        Genre genre = genreRepository.findGenreById(bookUpdateDto.genreId()).orElseThrow(
                () -> new GenreNotFoundException("Genre with id " + bookUpdateDto.genreId() + " not found")
        );

        bookToBeUpdated.setIsbn(bookUpdateDto.isbn());
        bookToBeUpdated.setTitle(bookUpdateDto.title());
        bookToBeUpdated.setPublicationDate(bookUpdateDto.publicationDate());
        bookToBeUpdated.setPrice(bookUpdateDto.price());
        bookToBeUpdated.setGenre(genre);

        // Remove authors that are not in the updated list
        List<Author> authorsToRemove = bookToBeUpdated.getAuthors().stream()
                .filter(author -> !bookUpdateDto.authorIds().contains(author.getAuthorId()))
                .toList();
        authorsToRemove.forEach(bookToBeUpdated::removeAuthor);

        // Add authors that are in updated list and not already in authors list of book
        bookUpdateDto.authorIds().stream()
                .map(authorId -> authorRepository.findAuthorById(authorId).orElseThrow(
                        () -> new AuthorNotFoundException("Author with id " + authorId + " not found")
                ))
                .filter(author -> !bookToBeUpdated.getAuthors().contains(author))
                .toList()
                .forEach(bookToBeUpdated::addAuthor);

        return bookDtoFromBook(bookToBeUpdated);
    }

    @Override
    @Transactional
    public BookDto deleteBook(Long id) throws BookNotFoundException {
        Book book = bookRepository.findBookById(id).orElseThrow(
                () -> new BookNotFoundException("Book with id " + id + " not found")
        );

        bookRepository.delete(book);

        return bookDtoFromBook(book);
    }
    
    @Override
    @Transactional
    public Long createBook(BookCreateDto bookCreateDto) throws IsbnAlreadyExistsException, AuthorNotFoundException,
            InvalidBookCreationException, GenreNotFoundException {
        if(bookCreateDto.authorIds().size() == 0) {
            throw new InvalidBookCreationException("Can not create book without author");
        }

        // Check if there is already a book with provided isbn
        Optional<Book> bookForProvidedIsbn = bookRepository.findBookByIsbn(bookCreateDto.isbn());

        if(bookForProvidedIsbn.isPresent()) {
            throw new IsbnAlreadyExistsException("There is already a book with isbn " + bookCreateDto.isbn());
        }

        Book bookToCreate = bookFromBookCreateDto(bookCreateDto);

        // Get authors by id and add them to book
        bookCreateDto.authorIds()
                .stream()
                .map(
                    authorId -> authorRepository.findAuthorById(authorId).orElseThrow(
                        () -> new AuthorNotFoundException("Author with id " + authorId + " not found")
                    )
                )
                .forEach(bookToCreate::addAuthor);

        bookRepository.add(bookToCreate);

        return bookToCreate.getBookId();
    }

    private BookDto bookDtoFromBook(Book book) {
        return BookDto.builder()
                .withId(book.getBookId())
                .withIsbn(book.getIsbn())
                .withTitle(book.getTitle())
                .withPublicationDate(book.getPublicationDate())
                .withPrice(book.getPrice())
                .withGenre(book.getGenre().getName())
                .withAuthorNames(
                    book.getAuthors()
                            .stream()
                            .map(author -> author.getFirstName() + " " + author.getLastName())
                            .collect(Collectors.toList())
                )
                .build();
    }

    private Book bookFromBookCreateDto(BookCreateDto bookCreateDto) throws GenreNotFoundException {
        // TODO: Test exception
        Genre genre = genreRepository.findGenreById(bookCreateDto.genreId()).orElseThrow(
                () -> new GenreNotFoundException("Genre with id " + bookCreateDto.genreId() + " not found")
        );

        return new Book(
            bookCreateDto.isbn(),
            bookCreateDto.title(),
            bookCreateDto.publicationDate(),
            bookCreateDto.price(),
            genre
        );
    }
}
