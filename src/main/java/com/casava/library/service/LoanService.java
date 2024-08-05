package com.casava.library.service;

import com.casava.library.dto.request.LoanRequestDTO;
import com.casava.library.dto.response.BookResponseDTO;
import com.casava.library.dto.response.LoanResponseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LoanService {
    List<LoanResponseDTO> getAllLoans();
    LoanResponseDTO getLoanById(UUID id);
    LoanResponseDTO recordLoan(LoanRequestDTO loanRequestDTO);
    LoanResponseDTO returnLoan(UUID id, LocalDate returnDate);
    List<BookResponseDTO> getBooksLoanedByUser(UUID userId);
}
