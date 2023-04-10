package at.fhv.msp.bookmanagementapplication.view;

import at.fhv.msp.bookmanagementapplication.application.api.BookService;
import at.fhv.msp.bookmanagementapplication.application.api.exception.BookNotFoundException;
import at.fhv.msp.bookmanagementapplication.application.dto.book.BookDto;
import org.apache.commons.lang3.StringUtils;
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
    public ResponseEntity<BookDto[]> getBooks(@RequestParam(required = false) String isbn) throws BookNotFoundException {
        if(isbn == null || StringUtils.isEmpty(isbn)) {
            List<BookDto> bookDtoList = bookService.getAllBooks();
            BookDto[] bookDtoArray = new BookDto[bookDtoList.size()];
            return ResponseEntity.ok().body(bookDtoList.toArray(bookDtoArray));
        } else {
            List<BookDto> bookDtoList = List.of(bookService.getBookByIsbn(isbn));
            BookDto[] bookDtoArray = new BookDto[bookDtoList.size()];
            return ResponseEntity.ok().body(bookDtoList.toArray(bookDtoArray));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) throws BookNotFoundException {
        BookDto bookDto = bookService.getBookById(id);
        return ResponseEntity.ok().body(bookDto);
    }
}
