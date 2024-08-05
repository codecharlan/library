package com.casava.library.controller;

import com.casava.library.advice.InternalCode;
import com.casava.library.dto.request.LoanRequestDTO;
import com.casava.library.dto.response.ApiResponseDTO;
import com.casava.library.dto.response.BookResponseDTO;
import com.casava.library.dto.response.LoanResponseDTO;
import com.casava.library.dto.response.UserResponseDTO;
import com.casava.library.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.casava.library.constant.Constants.SUCCESS_MESSAGE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loans")
@Tag(name = "Loan", description = "Loan management APIs")
public class LoanController {

    private final LoanService loanService;

    @GetMapping
    @Operation(summary = "Get all loans", description = "Retrieves a list of all loans")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = LoanResponseDTO.class)))
    public ResponseEntity<ApiResponseDTO<List<LoanResponseDTO>>> getAllLoans() {
        List<LoanResponseDTO> loans = loanService.getAllLoans();
        ApiResponseDTO<List<LoanResponseDTO>> response = new ApiResponseDTO<>(true,
                SUCCESS_MESSAGE, InternalCode.CASAVA_LIB_001, loans);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a loan by ID", description = "Retrieves a loan by its ID")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = LoanResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Loan not found")
    public ResponseEntity<ApiResponseDTO<LoanResponseDTO>> getLoanById(
            @Parameter(description = "ID of the loan to retrieve") @PathVariable UUID id) {
        LoanResponseDTO loan = loanService.getLoanById(id);
        ApiResponseDTO<LoanResponseDTO> response = new ApiResponseDTO<>(true,
                SUCCESS_MESSAGE, InternalCode.CASAVA_LIB_001, loan);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Record a new loan", description = "Creates a new loan record")
    @ApiResponse(responseCode = "201", description = "Loan recorded successfully",
            content = @Content(schema = @Schema(implementation = LoanResponseDTO.class)))
    public ResponseEntity<ApiResponseDTO<LoanResponseDTO>> recordLoan(
            @Parameter(description = "Loan details") @RequestBody @Valid LoanRequestDTO loanRequestDTO) {
        LoanResponseDTO loan = loanService.recordLoan(loanRequestDTO);
        ApiResponseDTO<LoanResponseDTO> response = new ApiResponseDTO<>(true,
                SUCCESS_MESSAGE, InternalCode.CASAVA_LIB_001, loan);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/return")
    @Operation(summary = "Return a loan", description = "Marks a loan as returned")
    @ApiResponse(responseCode = "200", description = "Loan returned successfully",
            content = @Content(schema = @Schema(implementation = LoanResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Loan not found")
    public ResponseEntity<ApiResponseDTO<LoanResponseDTO>> returnLoan(
            @Parameter(description = "ID of the loan to return") @PathVariable UUID id,
            @Parameter(description = "Date of return") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate) {
        LoanResponseDTO loan = loanService.returnLoan(id, returnDate);
        ApiResponseDTO<LoanResponseDTO> response = new ApiResponseDTO<>(true,
                SUCCESS_MESSAGE, InternalCode.CASAVA_LIB_001, loan);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get books loaned by user", description = "Retrieves a list of books loaned by a specific user")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = BookResponseDTO.class)))
    public ResponseEntity<ApiResponseDTO<List<BookResponseDTO>>> getBooksLoanedByUser(
            @Parameter(description = "ID of the user") @PathVariable UUID userId) {
        List<BookResponseDTO> books = loanService.getBooksLoanedByUser(userId);
        ApiResponseDTO<List<BookResponseDTO>> response = new ApiResponseDTO<>(true,
                SUCCESS_MESSAGE, InternalCode.CASAVA_LIB_001, books);
        return ResponseEntity.ok(response);
    }
}