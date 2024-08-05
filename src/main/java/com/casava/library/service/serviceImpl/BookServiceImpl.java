package com.casava.library.service.serviceImpl;

import com.casava.library.dto.request.BookRequestDTO;
import com.casava.library.dto.response.BookResponseDTO;
import com.casava.library.entity.Book;
import com.casava.library.exception.ResourceAlreadyExistException;
import com.casava.library.exception.ResourceNotFoundException;
import com.casava.library.repository.BookRepository;
import com.casava.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.casava.library.constant.Constants.BOOK_NOT_FOUND_MESSAGE;
import static com.casava.library.service.serviceImpl.LoanServiceImpl.getBookResponseDTO;
import static com.casava.library.util.Validator.validateRequest;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<BookResponseDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponseDTO getBookById(UUID id) {
        validateRequest(id);

        Book book = bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(BOOK_NOT_FOUND_MESSAGE));
        return toResponseDTO(book);
    }

    @Override
    public BookResponseDTO addBook(BookRequestDTO bookRequestDTO) {
        validateRequest(bookRequestDTO);
        Optional.ofNullable(bookRequestDTO.getIsbn())
                .filter(isbn -> !bookRepository.existsByIsbn(isbn))
                .orElseThrow(() -> new ResourceAlreadyExistException("Book with this isbn: " + bookRequestDTO.getIsbn() +
                        " already exists"));

        Book book = toEntity(bookRequestDTO);
        book = bookRepository.save(book);
        return toResponseDTO(book);
    }

    @Override
    public BookResponseDTO updateBook(UUID id, BookRequestDTO bookRequestDTO) {
        validateRequest(id);
        validateRequest(bookRequestDTO);

        Book existingBook = bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(BOOK_NOT_FOUND_MESSAGE));

        updateBookDetails(existingBook, bookRequestDTO);
        bookRepository.save(existingBook);
        return toResponseDTO(existingBook);
    }

    private void updateBookDetails(Book book, BookRequestDTO bookRequestDTO) {
        book.setTitle(bookRequestDTO.getTitle());
        book.setAuthor(bookRequestDTO.getAuthor());
        book.setPublishedYear(bookRequestDTO.getPublishedYear());
        book.setIsbn(bookRequestDTO.getIsbn());
        book.setCopiesAvailable(bookRequestDTO.getCopiesAvailable());
    }

    @Override
    public void deleteBook(UUID id) {
        validateRequest(id);

        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException(BOOK_NOT_FOUND_MESSAGE);
        }
        bookRepository.deleteById(id);
    }

    private BookResponseDTO toResponseDTO(Book book) {
        return getBookResponseDTO(book);
    }

    private Book toEntity(BookRequestDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPublishedYear(dto.getPublishedYear());
        book.setIsbn(dto.getIsbn());
        book.setCopiesAvailable(dto.getCopiesAvailable());
        return book;
    }
}
