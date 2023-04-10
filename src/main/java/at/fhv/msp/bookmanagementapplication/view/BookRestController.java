package at.fhv.msp.bookmanagementapplication.view;

import at.fhv.msp.bookmanagementapplication.application.api.BookService;
import at.fhv.msp.bookmanagementapplication.application.dto.book.BookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookRestController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<BookDto[]> getAllBooks() {
        List<BookDto> bookDtoList = bookService.getAllBooks();
        BookDto[] bookDtoArray = new BookDto[bookDtoList.size()];
        return ResponseEntity.ok().body(bookDtoList.toArray(bookDtoArray));
    }
}
