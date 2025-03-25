package com.example.bookstore.repository;

import com.example.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  // Marks this interface as a Spring Data repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // Extends the JpaRepository to provide CRUD operations
}
