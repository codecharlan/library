package com.casava.library.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class LoanRequestDTO {
    @NotNull(message = "Book ID is required")
    private UUID bookId;

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Loan date is required")
    @PastOrPresent(message = "Loan date must be in the past or present")
    private LocalDate loanDate;
}
