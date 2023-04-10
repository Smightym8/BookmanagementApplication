package at.fhv.msp.bookmanagementapplication.application.api;


import at.fhv.msp.bookmanagementapplication.application.api.exception.BookNotFoundException;
import at.fhv.msp.bookmanagementapplication.application.dto.book.BookCreateDto;
import at.fhv.msp.bookmanagementapplication.application.dto.book.BookDto;

import java.util.List;

public interface BookService {
    List<BookDto> getAllBooks();
    BookDto getBookById(Long id) throws BookNotFoundException;
    BookDto getBookByIsbn(String isbn) throws BookNotFoundException;
    Long createBook(BookCreateDto bookCreateDto) throws IllegalArgumentException;
}
