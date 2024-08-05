package com.casava.library.controller;

import com.casava.library.advice.InternalCode;
import com.casava.library.dto.request.BookRequestDTO;
import com.casava.library.dto.response.ApiResponseDTO;
import com.casava.library.dto.response.BookResponseDTO;
import com.casava.library.dto.response.UserResponseDTO;
import com.casava.library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.casava.library.constant.Constants.SUCCESS_MESSAGE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
@Tag(name = "Book", description = "Book management APIs")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieves a list of all books")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = BookResponseDTO.class)))
    public ResponseEntity<ApiResponseDTO<List<BookResponseDTO>>> getAllBooks() {
        List<BookResponseDTO> books = bookService.getAllBooks();
        ApiResponseDTO<List<BookResponseDTO>> response = new ApiResponseDTO<>(true,
                SUCCESS_MESSAGE, InternalCode.CASAVA_LIB_001, books);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by ID", description = "Retrieves a book by its ID")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = BookResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Book not found")
    public ResponseEntity<ApiResponseDTO<BookResponseDTO>> getBookById(
            @Parameter(description = "ID of the book to retrieve") @PathVariable UUID id) {
        BookResponseDTO book = bookService.getBookById(id);
        ApiResponseDTO<BookResponseDTO> response = new ApiResponseDTO<>(true,
                SUCCESS_MESSAGE, InternalCode.CASAVA_LIB_001, book);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Add a new book", description = "Creates a new book")
    @ApiResponse(responseCode = "201", description = "Book created successfully",
            content = @Content(schema = @Schema(implementation = BookResponseDTO.class)))
    public ResponseEntity<ApiResponseDTO<BookResponseDTO>> addBook(
            @Parameter(description = "Book details") @RequestBody @Valid BookRequestDTO bookRequestDTO) {
        BookResponseDTO book = bookService.addBook(bookRequestDTO);
        ApiResponseDTO<BookResponseDTO> response = new ApiResponseDTO<>(true,
                SUCCESS_MESSAGE, InternalCode.CASAVA_LIB_001, book);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book", description = "Updates an existing book")
    @ApiResponse(responseCode = "200", description = "Book updated successfully",
            content = @Content(schema = @Schema(implementation = BookResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Book not found")
    public ResponseEntity<ApiResponseDTO<BookResponseDTO>> updateBook(
            @Parameter(description = "ID of the book to update") @PathVariable UUID id,
            @Parameter(description = "Updated book details") @RequestBody @Valid BookRequestDTO bookRequestDTO) {
        BookResponseDTO book = bookService.updateBook(id, bookRequestDTO);
        ApiResponseDTO<BookResponseDTO> response = new ApiResponseDTO<>(true,
                SUCCESS_MESSAGE, InternalCode.CASAVA_LIB_001, book);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Deletes a book")
    @ApiResponse(responseCode = "204", description = "Book deleted successfully")
    @ApiResponse(responseCode = "404", description = "Book not found")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "ID of the book to delete") @PathVariable UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}