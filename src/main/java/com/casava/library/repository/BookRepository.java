package com.casava.library.repository;

import com.casava.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    boolean existsByIsbn(String isbn);
}