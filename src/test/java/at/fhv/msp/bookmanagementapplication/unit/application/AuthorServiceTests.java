package at.fhv.msp.bookmanagementapplication.unit.application;

import at.fhv.msp.bookmanagementapplication.application.api.AuthorService;
import at.fhv.msp.bookmanagementapplication.application.api.exception.AuthorNotFoundException;
import at.fhv.msp.bookmanagementapplication.application.dto.author.AuthorCreateDto;
import at.fhv.msp.bookmanagementapplication.application.dto.author.AuthorDto;
import at.fhv.msp.bookmanagementapplication.application.dto.author.AuthorUpdateDto;
import at.fhv.msp.bookmanagementapplication.domain.model.Author;
import at.fhv.msp.bookmanagementapplication.domain.model.Book;
import at.fhv.msp.bookmanagementapplication.domain.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuthorServiceTests {
    @Autowired
    private AuthorService authorService;

    @MockBean
    private AuthorRepository authorRepository;

    @Test
    void given_4authorsInRepository_when_getAllAuthors_then_return_expectedDtos() {
        // given
        List<Author> authorsExpected = List.of(
                new Author("John", "Doe"),
                new Author("Jane", "Doe"),
                new Author("Max", "Mustermann"),
                new Author("Maria", "Musterfrau")
        );

        for(int i = 0; i < authorsExpected.size(); i++) {
            authorsExpected.get(i).setAuthorId((long) i);
        }

        Mockito.when(authorRepository.findAllAuthors()).thenReturn(authorsExpected);

        // when
        List<AuthorDto> authorsActual = authorService.getAllAuthors();

        // then
        assertEquals(authorsExpected.size(), authorsActual.size());

        for(int i = 0; i < authorsExpected.size(); i++) {
            Author authorExpected = authorsExpected.get(i);
            AuthorDto authorActual = authorsActual.get(i);

            assertEquals(authorExpected.getFirstName(), authorActual.firstName());
            assertEquals(authorExpected.getLastName(), authorActual.lastName());
        }
    }

    @Test
    void given_authorInRepository_when_getAuthorById_then_return_expectedDto() {
        // given
        Long authorIdExpected = 42L;
        Author authorExpected = new Author("John", "Doe");
        authorExpected.setAuthorId(authorIdExpected);
        Book bookExpected =new Book(
                "1234567891234",
                "A reference book",
                LocalDate.of(2011,4,20),
                new BigDecimal("38.93"),
                "Reference book"
        );
        bookExpected.addAuthor(authorExpected);

        Mockito.when(authorRepository.findAuthorById(authorIdExpected)).thenReturn(Optional.of(authorExpected));

        // when
        AuthorDto authorActual = authorService.getAuthorById(authorIdExpected);

        // then
        assertEquals(authorExpected.getFirstName(), authorActual.firstName());
        assertEquals(authorExpected.getLastName(), authorActual.lastName());
        assertTrue(authorActual.bookNames().contains(bookExpected.getTitle()));
    }

    @Test
    void given_nonExistentAuthorId_when_getAuthorById_then_AuthorNotFoundExceptionIsThrown() {
        // given
        Long authorIdExpected = 42L;
        Mockito.when(authorRepository.findAuthorById(authorIdExpected)).thenReturn(Optional.empty());

        // when ... then
        assertThrows(AuthorNotFoundException.class, () -> authorService.getAuthorById(authorIdExpected));
    }

    @Test
    void given_authorCreateDto_when_createAuthor_then_addIsCalled() {
        // given
        AuthorCreateDto authorCreateDto = AuthorCreateDto.builder()
                .withFirstName("John")
                .withLastName("Doe")
                .build();

        Author author = new Author(authorCreateDto.firstName(), authorCreateDto.lastName());

        Mockito.doNothing().when(authorRepository).add(author);

        // when
        authorService.createAuthor(authorCreateDto);

        // then
        Mockito.verify(authorRepository, Mockito.times(1)).add(author);
    }

    @Test
    void given_authorId_when_deleteAuthor_then_deleteIsCalled() {
        // given
        Long authorId = 42L;
        Author author = new Author("John", "Doe");
        author.setAuthorId(authorId);

        Mockito.when(authorRepository.findAuthorById(authorId)).thenReturn(Optional.of(author));
        Mockito.doNothing().when(authorRepository).delete(author);

        // when
        authorService.deleteAuthor(authorId);

        // then
        Mockito.verify(authorRepository, Mockito.times(1)).delete(author);
    }

    @Test
    void given_nonExistentAuthorId_when_deleteAuthor_then_AuthorNotFoundExceptionIsThrown() {
        // given
        Long authorId = 42L;

        Mockito.when(authorRepository.findAuthorById(authorId)).thenReturn(Optional.empty());

        // when ... then
        assertThrows(AuthorNotFoundException.class, () -> authorService.deleteAuthor(authorId));
    }

    @Test
    void given_authorIdAndAuthorUpdateDto_when_updateAuthor_then_authorIsUpdated() {
        // given
        Long authorId = 42L;
        String lastNameExpected = "Doe Doe";
        Author author = new Author("John", "Doe");
        author.setAuthorId(authorId);

        AuthorUpdateDto authorUpdateDto = AuthorUpdateDto.builder()
                .withFirstName("John")
                .withLastName(lastNameExpected)
                .build();

        Mockito.when(authorRepository.findAuthorById(authorId)).thenReturn(Optional.of(author));

        // when
        AuthorDto authorActual = authorService.updateAuthor(authorId, authorUpdateDto);

        // then
        assertEquals(author.getFirstName(), authorActual.firstName());
        assertEquals(lastNameExpected, authorActual.lastName());
    }

    @Test
    void given_nonExistentAuthorIdAndAuthorUpdateDto_when_updateAuthor_then_AuthorNotFoundExceptionIsThrown() {
        // given
        Long authorId = 42L;
        AuthorUpdateDto authorUpdateDto = AuthorUpdateDto.builder()
                .withFirstName("John")
                .withLastName("Doe Doe")
                .build();

        Mockito.when(authorRepository.findAuthorById(authorId)).thenReturn(Optional.empty());

        // when ... then
        assertThrows(AuthorNotFoundException.class, () -> authorService.updateAuthor(authorId, authorUpdateDto));
    }
}
