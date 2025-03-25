package com.example.bookstore.controller;

import com.example.bookstore.entity.Book;
import com.example.bookstore.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class) // This sets up the test for BookController only
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simulates HTTP requests

    @MockBean
    private BookService bookService; // Mocks the service layer (avoids hitting real DB)

    @Autowired
    private ObjectMapper objectMapper; // Converts objects to JSON

    // Sample book object for testing
    private final Book sampleBook = new Book(
    	    "One of Us Is Lying", 
    	    "Karen M. McManus", 
    	    new BigDecimal("375"), 
    	    LocalDate.of(2017, 5, 10)
    	);

    @Test
    void testGetBookById() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(sampleBook); // Mocking service response

        mockMvc.perform(get("/books/1")) // Simulate GET request
                .andExpect(status().isOk()) // Expect 200 OK response
                .andExpect(jsonPath("$.title").value("One of Us Is Lying")) // Check title
                .andExpect(jsonPath("$.author").value("Karen M. McManus")); // Check author
    }

    @Test
    void testGetAllBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of(sampleBook)); // Return a single book list

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.size()").value(1)) // Ensure list has one book
                .andExpect(jsonPath("$[0].title").value("One of Us Is Lying")); // Check book title
    }

    @Test
    void testAddBook() throws Exception {
        when(bookService.addBook(any(Book.class))).thenReturn(sampleBook); // Mock book addition

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON) // Send JSON
                .content(objectMapper.writeValueAsString(sampleBook))) // Convert sampleBook to JSON
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.title").value("One of Us Is Lying")); // Confirm book title
    }

    @Test
    void testUpdateBook() throws Exception {
        when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(sampleBook); // Mock update service

        mockMvc.perform(put("/books/1") // Simulating a PUT request to update book with ID 1
                .contentType(MediaType.APPLICATION_JSON) // Setting content type as JSON
                .content(objectMapper.writeValueAsString(sampleBook))) // Converting sampleBook to JSON for request
                .andExpect(status().isOk()) // Expect 200 OK response
                .andExpect(jsonPath("$.title").value("One of Us Is Lying")); // Ensure the title is updated

        verify(bookService, times(1)).updateBook(eq(1L), any(Book.class)); // Ensure updateBook() is called once
    }

    @Test
    void testDeleteBook() throws Exception {
        doNothing().when(bookService).deleteBook(1L); // Mock delete action

        mockMvc.perform(delete("/books/1")) // Simulate DELETE request
                .andExpect(status().isOk()); // Expect 200 OK

        verify(bookService, times(1)).deleteBook(1L); // Ensure service method was called once
    }
}
