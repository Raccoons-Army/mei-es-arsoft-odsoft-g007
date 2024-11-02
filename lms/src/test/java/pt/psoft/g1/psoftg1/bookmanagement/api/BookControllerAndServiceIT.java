package pt.psoft.g1.psoftg1.bookmanagement.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.CreateBookRequest;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.shared.model.Photo;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class BookControllerAndServiceIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final Book bookMock = mock(Book.class);
    private final BookRepository bookRepository = mock(BookRepository.class);
    private final GenreRepository genreRepository = mock(GenreRepository.class);
    private final String isbn = "1234567890";

    @BeforeEach
    void setUp() {

    }

    @Test
    void testCreateBook_Successful() throws Exception {

        CreateBookRequest createBookRequest = mock(CreateBookRequest.class);
        when(createBookRequest.getTitle()).thenReturn("Test Book");
        when(createBookRequest.getDescription()).thenReturn("A test book description.");
        when(createBookRequest.getAuthors()).thenReturn(Arrays.asList("Author 1", "Author 2"));
        when(createBookRequest.getGenre()).thenReturn("Test Genre");

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

        when(bookMock.getIsbn()).thenReturn(isbn);
        when(bookMock.getTitle()).thenReturn("Test Book");
        when(bookMock.getDescription()).thenReturn("A test book description.");
        when(bookMock.getAuthors()).thenReturn(Arrays.asList(mock(Author.class), mock(Author.class)));
        when(bookMock.getGenre()).thenReturn(mock(Genre.class));
        when(bookMock.getPk()).thenReturn("cjwj2cn32");
        when(bookMock.getPhoto()).thenReturn(mock(Photo.class));

        when(bookRepository.save(bookMock)).thenReturn(bookMock);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.title").value(createBookRequest.getTitle()));
    }

    @Test
    void testCreateBook_IsbnExistent() throws Exception {

        CreateBookRequest createBookRequest = mock(CreateBookRequest.class);
        when(createBookRequest.getTitle()).thenReturn("Test Book");
        when(createBookRequest.getDescription()).thenReturn("A test book description.");
        when(createBookRequest.getAuthors()).thenReturn(Arrays.asList("Author 1", "Author 2"));
        when(createBookRequest.getGenre()).thenReturn("Test Genre");

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(bookMock));

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateBook_BadRequest() throws Exception {

        CreateBookRequest createBookRequest = mock(CreateBookRequest.class);
        when(createBookRequest.getTitle()).thenReturn(null);
        when(createBookRequest.getDescription()).thenReturn("A test book description.");
        when(createBookRequest.getAuthors()).thenReturn(Arrays.asList("Author 1", "Author 2"));
        when(createBookRequest.getGenre()).thenReturn("Test Genre");

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateBook_GenreNotFound() throws Exception {
        String genre = "Test Genre";
        CreateBookRequest createBookRequest = mock(CreateBookRequest.class);
        when(createBookRequest.getTitle()).thenReturn("Test Book");
        when(createBookRequest.getDescription()).thenReturn("A test book description.");
        when(createBookRequest.getAuthors()).thenReturn(Arrays.asList("Author 1", "Author 2"));
        when(createBookRequest.getGenre()).thenReturn(genre);

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        when(genreRepository.findByString(genre)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateBook_Successful() throws Exception {

        CreateBookRequest createBookRequest = mock(CreateBookRequest.class);
        when(createBookRequest.getTitle()).thenReturn("Test Book");
        when(createBookRequest.getDescription()).thenReturn("A test book description.");
        when(createBookRequest.getAuthors()).thenReturn(Arrays.asList("Author 1", "Author 2"));
        when(createBookRequest.getGenre()).thenReturn("Test Genre");

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(bookMock));

        when(bookMock.getIsbn()).thenReturn(isbn);
        when(bookMock.getTitle()).thenReturn("Test Book");
        when(bookMock.getDescription()).thenReturn("A test book description.");
        when(bookMock.getAuthors()).thenReturn(Arrays.asList(mock(Author.class), mock(Author.class)));
        when(bookMock.getGenre()).thenReturn(mock(Genre.class));
        when(bookMock.getPk()).thenReturn("cjwj2cn32");
        when(bookMock.getPhoto()).thenReturn(mock(Photo.class));

        when(bookRepository.save(bookMock)).thenReturn(bookMock);

        mockMvc.perform(put("/api/books/{isbn}", isbn)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.title").value(createBookRequest.getTitle()));
    }

    @Test
    void testGetBookByIsbn() throws Exception {
        when(bookMock.getIsbn()).thenReturn(isbn);
        when(bookMock.getTitle()).thenReturn("Test Book");

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(bookMock));
        mockMvc.perform(get("/api/books/{isbn}", bookMock.getIsbn()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(bookMock.getTitle()));
    }

    @Test
    void testGetBookByIsbn_NotFound() throws Exception {
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/books/{isbn}", isbn))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteBookPhoto() throws Exception {

        // Set the photo URI of the book
        when(bookMock.getIsbn()).thenReturn(isbn);
        when(bookMock.getPhoto()).thenReturn(mock(Photo.class));
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(bookMock));

        mockMvc.perform(delete("/api/books/{isbn}/photo", bookMock.getIsbn()))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteBookPhoto_NotFound() throws Exception {
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/books/{isbn}/photo", isbn))
                .andExpect(status().isNotFound());
    }
}
