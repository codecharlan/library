package com.casava.library.service.serviceImpl;

import com.casava.library.dto.request.BookRequestDTO;
import com.casava.library.dto.response.BookResponseDTO;
import com.casava.library.entity.Book;
import com.casava.library.exception.ResourceAlreadyExistException;
import com.casava.library.exception.ResourceNotFoundException;
import com.casava.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book testBook;
    private BookRequestDTO testBookRequestDTO;
    private UUID testId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testId = UUID.randomUUID();
        testBook = new Book();
        testBook.setId(testId);
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setPublishedYear(2021);
        testBook.setIsbn("1234567890");
        testBook.setCopiesAvailable(5);

        testBookRequestDTO = new BookRequestDTO();
        testBookRequestDTO.setTitle("Test Book");
        testBookRequestDTO.setAuthor("Test Author");
        testBookRequestDTO.setPublishedYear(2021);
        testBookRequestDTO.setIsbn("1234567890");
        testBookRequestDTO.setCopiesAvailable(5);
    }

    @Test
    void getAllBooks_ShouldReturnListOfBookResponseDTOs() {
        when(bookRepository.findAll()).thenReturn(Collections.singletonList(testBook));

        List<BookResponseDTO> result = bookService.getAllBooks();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(testBook.getTitle(), result.get(0).getTitle());
    }

    @Test
    void getBookById_WithValidId_ShouldReturnBookResponseDTO() {
        when(bookRepository.findById(testId)).thenReturn(Optional.of(testBook));

        BookResponseDTO result = bookService.getBookById(testId);

        assertNotNull(result);
        assertEquals(testBook.getTitle(), result.getTitle());
    }

    @Test
    void getBookById_WithInvalidId_ShouldThrowResourceNotFoundException() {
        when(bookRepository.findById(testId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(testId));
    }

    @Test
    void addBook_WithValidData_ShouldReturnBookResponseDTO() {
        when(bookRepository.existsByIsbn(anyString())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookResponseDTO result = bookService.addBook(testBookRequestDTO);

        assertNotNull(result);
        assertEquals(testBookRequestDTO.getTitle(), result.getTitle());
    }

    @Test
    void addBook_WithExistingIsbn_ShouldThrowResourceAlreadyExistException() {
        when(bookRepository.existsByIsbn(anyString())).thenReturn(true);

        assertThrows(ResourceAlreadyExistException.class, () -> bookService.addBook(testBookRequestDTO));
    }

    @Test
    void updateBook_WithValidIdAndData_ShouldReturnUpdatedBookResponseDTO() {
        when(bookRepository.findById(testId)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookResponseDTO result = bookService.updateBook(testId, testBookRequestDTO);

        assertNotNull(result);
        assertEquals(testBookRequestDTO.getTitle(), result.getTitle());
    }

    @Test
    void updateBook_WithInvalidId_ShouldThrowResourceNotFoundException() {
        when(bookRepository.findById(testId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(testId, testBookRequestDTO));
    }

    @Test
    void deleteBook_WithValidId_ShouldDeleteBook() {
        when(bookRepository.existsById(testId)).thenReturn(true);

        assertDoesNotThrow(() -> bookService.deleteBook(testId));
        verify(bookRepository, times(1)).deleteById(testId);
    }

    @Test
    void deleteBook_WithInvalidId_ShouldThrowResourceNotFoundException() {
        when(bookRepository.existsById(testId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBook(testId));
    }
}