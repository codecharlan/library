package com.casava.library.service;

import com.casava.library.dto.request.BookRequestDTO;
import com.casava.library.dto.response.BookResponseDTO;

import java.util.List;
import java.util.UUID;

public interface BookService {
    List<BookResponseDTO> getAllBooks();
    BookResponseDTO getBookById(UUID id);
    BookResponseDTO addBook(BookRequestDTO bookRequestDTO);
    BookResponseDTO updateBook(UUID id, BookRequestDTO bookRequestDTO);
    void deleteBook(UUID id);
}
