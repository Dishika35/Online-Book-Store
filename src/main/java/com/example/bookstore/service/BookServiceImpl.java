package com.example.bookstore.service;

import com.example.bookstore.entity.Book;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.exception.ResourceNotFoundException;
import com.example.bookstore.util.MetricsTracker;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BookServiceImpl implements BookService {

    // Logger for printing information and error messages
    private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);
    
    // Repository to interact with the database
    private final BookRepository bookRepository;

    // Constructor injection of BookRepository
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book addBook(Book book) {
        // Start tracking the time for addBook operation
        MetricsTracker tracker = new MetricsTracker("addBook");
        try {
            // Log that we're adding a new book
            log.info("Adding new book: {}", book.getTitle());
            // Save the book to the database
            Book savedBook = bookRepository.save(book);
            // Log a success message with the book's ID and title
            log.info("New book added successfully with ID: {} and Name: {}", savedBook.getId(), savedBook.getTitle());
            return savedBook;
        } finally {
            // Log the time taken for this operation
            tracker.stop();
        }
    }

    @Override
    public List<Book> getAllBooks() {
        // Start tracking the time for getAllBooks operation
        MetricsTracker tracker = new MetricsTracker("getAllBooks");
        try {
            // Log that we're fetching all books
            log.info("Fetching all books...");
            // Get the list of books from the database
            List<Book> books = bookRepository.findAll();
            // Log the number of books fetched
            log.info("All books fetched successfully. Total books: {}", books.size());
            return books;
        } finally {
            // Log the time taken for this operation
            tracker.stop();
        }
    }

    @Override
    public Book getBookById(Long id) {
        // Start tracking the time for getBookById operation
        MetricsTracker tracker = new MetricsTracker("getBookById");
        try {
            // Log that we're fetching a book with a specific ID
            log.info("Fetching book with ID: {}", id);
            Optional<Book> optionalBook = bookRepository.findById(id);
            if (optionalBook.isPresent()) {
                Book book = optionalBook.get();
                // Log that the book was successfully retrieved
                log.info("Returning book with ID: {} and Name: {}", id, book.getTitle());
                return book;
            } else {
                // Log error and throw exception if book is not found
                log.error("Book with ID {} not found", id);
                throw new ResourceNotFoundException("Book not found with id " + id);
            }
        } finally {
            // Log the time taken for this operation
            tracker.stop();
        }
    }

    @Override
    public Book updateBook(Long id, Book updatedBook) {
        // Start tracking the time for updateBook operation
        MetricsTracker tracker = new MetricsTracker("updateBook");
        try {
            // Log that we're updating a book with a specific ID
            log.info("Updating book with ID: {}", id);
            Optional<Book> optionalBook = bookRepository.findById(id);
            if (optionalBook.isPresent()) {
                Book book = optionalBook.get();
                // Update the book's details
                book.setTitle(updatedBook.getTitle());
                book.setAuthor(updatedBook.getAuthor());
                book.setPrice(updatedBook.getPrice());
                book.setPublishedDate(updatedBook.getPublishedDate());
                // Save the updated book
                Book savedBook = bookRepository.save(book);
                // Log that the update was successful
                log.info("Returning updated book with ID: {} and Name: {}", id, savedBook.getTitle());
                return savedBook;
            } else {
                // Log error and throw exception if book is not found
                log.error("Update failed: Book with ID {} not found", id);
                throw new ResourceNotFoundException("Book not found with id " + id);
            }
        } finally {
            // Log the time taken for this operation
            tracker.stop();
        }
    }

    @Override
    public void deleteBook(Long id) {
        // Start tracking the time for deleteBook operation
        MetricsTracker tracker = new MetricsTracker("deleteBook");
        try {
            // Log that we're deleting a book with a specific ID
            log.info("Deleting book with ID: {}", id);
            if (!bookRepository.existsById(id)) {
                // Log error and throw exception if the book does not exist
                log.error("Delete failed: Book with ID {} not found", id);
                throw new ResourceNotFoundException("Book not found with id " + id);
            }
            // Delete the book from the database
            bookRepository.deleteById(id);
            // Log that the deletion was successful
            log.info("Book with ID {} deleted successfully", id);
        } finally {
            // Log the time taken for this operation
            tracker.stop();
        }
    }
}
