package at.fhv.msp.bookmanagementapplication.application.api;


import at.fhv.msp.bookmanagementapplication.application.api.exception.*;
import at.fhv.msp.bookmanagementapplication.application.dto.book.BookCreateDto;
import at.fhv.msp.bookmanagementapplication.application.dto.book.BookDto;
import at.fhv.msp.bookmanagementapplication.application.dto.book.BookUpdateDto;

import java.util.List;

public interface BookService {
    List<BookDto> getAllBooks();
    BookDto getBookById(Long id) throws BookNotFoundException;
    BookDto getBookByIsbn(String isbn) throws BookNotFoundException;
    BookDto updateBook(Long id, BookUpdateDto bookUpdateDto) throws BookNotFoundException,
            IsbnAlreadyExistsException, InvalidBookUpdateException, GenreNotFoundException;
    BookDto deleteBook(Long id) throws BookNotFoundException;
    Long createBook(BookCreateDto bookCreateDto) throws IsbnAlreadyExistsException, AuthorNotFoundException,
            InvalidBookCreationException, GenreNotFoundException;
}
