package com.example.bookstore.controller;

import com.example.bookstore.entity.Book;
import com.example.bookstore.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books") // Base URL for all book-related endpoints
public class BookController {

    private final BookService bookService;

    // Constructor-based dependency injection
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}") // Handles HTTP GET requests to retrieve a book by its ID
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping // Handles HTTP GET requests to retrieve all books
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @DeleteMapping("/{id}") // Handles HTTP DELETE requests to remove a book by its ID
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @PutMapping("/{id}") // Handles HTTP PUT requests to update an existing book
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.updateBook(id, book);
    }

    @PostMapping // Handles HTTP POST requests to add a new book
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }
}