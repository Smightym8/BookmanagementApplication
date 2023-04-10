package at.fhv.msp.bookmanagementapplication.application.api;


import at.fhv.msp.bookmanagementapplication.application.dto.book.BookDto;

import java.util.List;

public interface BookService {
    List<BookDto> getAllBooks();
}
