package com.example.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.bookstore.entity.Book;
import com.example.bookstore.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookServiceImpl bookService; // Service being tested

    @Mock
    private BookRepository bookRepository; // Mocked repository

    private Book book1, book2;

    @BeforeEach
    void setUp() {
        // Create some sample books for testing
        book1 = new Book("The Silent Patient", "Alex Michaelides", new BigDecimal("399"), LocalDate.of(2019, 2, 5));
        book2 = new Book("And Then There Were None", "Agatha Christie", new BigDecimal("250"), LocalDate.of(1939, 11, 6));
    }

    @Test
    void addBookTest() {
        when(bookRepository.save(book1)).thenReturn(book1); // Mock save behavior
        
        Book savedBook = bookService.addBook(book1); // Call service method
        
        // Make sure the saved book has the expected title and price
        assertEquals(book1.getTitle(), savedBook.getTitle());
        assertEquals(book1.getPrice(), savedBook.getPrice());
    }

    @Test
    void getAllBooksTest() {
        // Create a list of books that the repository should return
        List<Book> list = new ArrayList<>();
        list.add(book1);
        list.add(book2);

        when(bookRepository.findAll()).thenReturn(list); // Stub findAll()

        List<Book> books = bookService.getAllBooks(); // Fetch books from service

        // Check that the list size matches and the first book title is correct
        assertEquals(list.size(), books.size());
        assertEquals(list.get(0).getTitle(), books.get(0).getTitle());
    }

    @Test
    void deleteBookTest() {
        when(bookRepository.existsById(1L)).thenReturn(true); // Simulate existing book
        doNothing().when(bookRepository).deleteById(1L); // Stub delete behavior
        
        bookService.deleteBook(1L); // Call delete method
        
        // Verify that the repository delete method was called exactly once
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void findBookByIdTest() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1)); // Mock repository response

        Book foundBook = bookService.getBookById(1L); // Fetch book from service

        // Ensure a book was returned and its details match
        assertNotNull(foundBook);
        assertEquals(book1.getTitle(), foundBook.getTitle());
        assertEquals(book1.getPrice(), foundBook.getPrice());
    }

    @Test
    void updateBookTest() {
        // Create an updated book object with new details
        Book updatedBook = new Book("Spring Boot Advanced", "John Doe", new BigDecimal("550.00"), LocalDate.of(2023, 1, 1));
        updatedBook.setId(1L); // Ensure the ID remains the same

        // Simulate finding the existing book and saving the updated one
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        Book result = bookService.updateBook(1L, updatedBook); // Call update method

        // Verify that the returned book has the updated details
        assertNotNull(result);
        assertEquals("Spring Boot Advanced", result.getTitle());
        assertEquals(new BigDecimal("550.00"), result.getPrice());

        // Capture the saved book using ArgumentCaptor to check what was actually saved
        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());

        Book savedBook = bookCaptor.getValue();
        assertEquals("Spring Boot Advanced", savedBook.getTitle());
        assertEquals(new BigDecimal("550.00"), savedBook.getPrice());
        assertEquals(LocalDate.of(2023, 1, 1), savedBook.getPublishedDate());
    }
}
