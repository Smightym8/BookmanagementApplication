package at.fhv.msp.bookmanagementapplication.unit.application;

import at.fhv.msp.bookmanagementapplication.application.api.AuthorService;
import at.fhv.msp.bookmanagementapplication.application.api.exception.AuthorNotFoundException;
import at.fhv.msp.bookmanagementapplication.application.dto.author.AuthorDto;
import at.fhv.msp.bookmanagementapplication.domain.model.Author;
import at.fhv.msp.bookmanagementapplication.domain.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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
    void given_authorInRepository_when_getAuthorById_then_return_expectedDto() {
        // given
        Long authorIdExpected = 42L;
        Author authorExpected = new Author("John", "Doe");
        authorExpected.setAuthorId(authorIdExpected);

        Mockito.when(authorRepository.findAuthorById(authorIdExpected)).thenReturn(Optional.of(authorExpected));

        // when
        AuthorDto authorActual = authorService.getAuthorById(authorIdExpected);

        // then
        assertEquals(authorExpected.getFirstName(), authorActual.firstName());
        assertEquals(authorExpected.getLastName(), authorActual.lastName());
    }

    @Test
    void given_nonExistentAuthorId_when_getAuthorById_then_AuthorNotFoundExceptionIsThrown() {
        // given
        Long authorIdExpected = 42L;
        Mockito.when(authorRepository.findAuthorById(authorIdExpected)).thenReturn(Optional.empty());

        // when ... then
        assertThrows(AuthorNotFoundException.class, () -> authorService.getAuthorById(authorIdExpected));
    }
}
