package com.example.bookstore.service;

import com.example.bookstore.entity.Book;
import java.util.List;

// Service interface defining the operations for managing books
public interface BookService {
    Book addBook(Book book);  // Adds a new book
    List<Book> getAllBooks();  // Retrieves all books
    Book getBookById(Long id);  // Retrieves a book by its ID
    Book updateBook(Long id, Book book);  // Updates an existing book
    void deleteBook(Long id);  // Deletes a book by its ID
}