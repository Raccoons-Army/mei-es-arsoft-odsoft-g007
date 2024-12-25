package pt.psoft.g1.psoftg1.suggestionmanagement.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.shared.idGenerationStrategy.IdGenerationStrategy;
import pt.psoft.g1.psoftg1.suggestionmanagement.model.Suggestion;
import pt.psoft.g1.psoftg1.suggestionmanagement.publishers.SuggestionEventPublisher;
import pt.psoft.g1.psoftg1.suggestionmanagement.repositories.SuggestionRepository;
import pt.psoft.g1.psoftg1.suggestionmanagement.services.CreateSuggestionRequest;
import pt.psoft.g1.psoftg1.suggestionmanagement.services.SuggestionServiceImpl;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SuggestionServiceBBTest {

    @Mock
    private SuggestionRepository suggestionRepository;
    @Mock
    private ReaderRepository readerRepository;
    @Mock
    private IdGenerationStrategy<String> idGenerationStrategy;
    @Mock
    private SuggestionEventPublisher suggestionEventPublisher;

    @InjectMocks
    private SuggestionServiceImpl suggestionService;

    private CreateSuggestionRequest request;

    @BeforeEach
    public void setup() {
        request = mock(CreateSuggestionRequest.class);
    }

    // Successfully create suggestion with valid ISBN and reader number
    @Test
    public void test_create_suggestion_success() {
        // Arrange
        String isbn = "9789896379636";
        String readerNumber = "2024/1";
        String generatedId = "S123";
        ReaderDetails reader = mock(ReaderDetails.class);

        when(request.getIsbn()).thenReturn(isbn);
        when(request.getReaderNumber()).thenReturn(readerNumber);
        when(suggestionRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        when(readerRepository.findByReaderNumber(readerNumber)).thenReturn(Optional.of(reader));
        when(idGenerationStrategy.generateId()).thenReturn(generatedId);
        when(suggestionRepository.save(any(Suggestion.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Suggestion result = suggestionService.createSuggestion(request);

        // Assert
        assertNotNull(result);
        assertEquals(isbn, result.getIsbn());
        assertEquals(reader, result.getReaderDetails());
    }

    // Fail to create suggestion with invalid ISBN
    @Test
    public void test_create_suggestion_fail_invalid_isbn() {
        // Arrange
        String isbn = "9789896379636";
        String readerNumber = "2024/1";

        when(request.getIsbn()).thenReturn(isbn);
        when(suggestionRepository.findByIsbn(isbn)).thenReturn(Optional.of(mock(Suggestion.class)));

        // Act and Assert
        assertThrows(ConflictException.class, () -> suggestionService.createSuggestion(request));
    }
}
