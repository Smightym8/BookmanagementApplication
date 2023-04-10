package at.fhv.msp.bookmanagementapplication.application.impl;


import at.fhv.msp.bookmanagementapplication.application.api.BookService;
import at.fhv.msp.bookmanagementapplication.application.api.exception.BookNotFoundException;
import at.fhv.msp.bookmanagementapplication.application.api.exception.IsbnAlreadyExistsException;
import at.fhv.msp.bookmanagementapplication.application.dto.book.BookCreateDto;
import at.fhv.msp.bookmanagementapplication.application.dto.book.BookDto;
import at.fhv.msp.bookmanagementapplication.application.dto.book.BookUpdateDto;
import at.fhv.msp.bookmanagementapplication.domain.model.Book;
import at.fhv.msp.bookmanagementapplication.domain.repository.BookRepository;
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
    public BookDto updateBook(Long id, BookUpdateDto bookUpdateDto) throws BookNotFoundException, IsbnAlreadyExistsException {
        Book bookToBeUpdated = bookRepository.findBookById(id).orElseThrow(
                () -> new BookNotFoundException("Book with id " + id + " not found")
        );

        // Check if there is already a book with provided isbn and it is not the book to be updated
        Optional<Book> bookForProvidedIsbn = bookRepository.findBookByIsbn(bookUpdateDto.isbn());

        if(bookForProvidedIsbn.isPresent() && !Objects.equals(bookToBeUpdated.getIsbn(), bookUpdateDto.isbn())) {
            throw new IsbnAlreadyExistsException("There is already a book with isbn " + bookUpdateDto.isbn());
        }

        bookToBeUpdated.update(
                bookToBeUpdated.getIsbn(),
                bookUpdateDto.title(),
                bookUpdateDto.publicationDate(),
                bookUpdateDto.price(),
                bookUpdateDto.genre()
        );

        return bookDtoFromBook(bookToBeUpdated);
    }

    @Override
    @Transactional
    public Long createBook(BookCreateDto bookCreateDto) throws IsbnAlreadyExistsException {
        // Check if there is already a book with provided isbn
        Optional<Book> bookForProvidedIsbn = bookRepository.findBookByIsbn(bookCreateDto.isbn());

        if(bookForProvidedIsbn.isPresent()) {
            throw new IsbnAlreadyExistsException("There is already a book with isbn " + bookCreateDto.isbn());
        }

        Book bookToCreate = bookFromBookCreateDto(bookCreateDto);

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
                .withGenre(book.getGenre())
                .build();
    }

    private Book bookFromBookCreateDto(BookCreateDto bookCreateDto) {
        return new Book(
            bookCreateDto.isbn(),
            bookCreateDto.title(),
            bookCreateDto.publicationDate(),
            bookCreateDto.price(),
            bookCreateDto.genre()
        );
    }
}
