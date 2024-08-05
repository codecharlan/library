package com.casava.library.service.serviceImpl;

import com.casava.library.dto.request.LoanRequestDTO;
import com.casava.library.dto.response.BookResponseDTO;
import com.casava.library.dto.response.LoanResponseDTO;
import com.casava.library.entity.Book;
import com.casava.library.entity.Loan;
import com.casava.library.exception.ResourceNotFoundException;
import com.casava.library.repository.BookRepository;
import com.casava.library.repository.LoanRepository;
import com.casava.library.repository.UserRepository;
import com.casava.library.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.casava.library.constant.Constants.*;
import static com.casava.library.util.Validator.validateRequest;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public List<LoanResponseDTO> getAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        return loans.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public LoanResponseDTO getLoanById(UUID id) {
        validateRequest(id);

        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(LOAN_NOT_FOUND_MESSAGE));
        return toResponseDTO(loan);
    }

    @Override
    public LoanResponseDTO recordLoan(LoanRequestDTO loanRequestDTO) {
        validateRequest(loanRequestDTO);
        if (!userRepository.existsById(loanRequestDTO.getUserId())) {
            throw new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE);
        }

        if (!bookRepository.existsById(loanRequestDTO.getBookId())) {
            throw new ResourceNotFoundException(BOOK_NOT_FOUND_MESSAGE);
        }
        Loan loan = toEntity(loanRequestDTO);
        loan = loanRepository.save(loan);
        return toResponseDTO(loan);
    }

    @Override
    public LoanResponseDTO returnLoan(UUID id, LocalDate returnDate) {
        validateRequest(id);
        validateRequest(returnDate);

        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(LOAN_NOT_FOUND_MESSAGE));
        if(returnDate.isBefore(loan.getLoanDate())){
            throw new IllegalArgumentException("Return date cannot be before loan date");
        }
        loan.setReturnDate(returnDate);
                loan = loanRepository.save(loan);
        return toResponseDTO(loan);
    }

    @Override
    public List<BookResponseDTO> getBooksLoanedByUser(UUID userId) {
        validateRequest(userId);

        List<Loan> loans = loanRepository.findByUserId(userId);
        return loans.stream()
                .sorted(Comparator.comparing(Loan::getLoanDate).reversed())
                .map(loan -> bookRepository.findById(loan.getBookId())
                        .map(this::toBookResponseDTO)
                        .orElseThrow(() -> new ResourceNotFoundException(BOOK_NOT_FOUND_MESSAGE)))
                .collect(Collectors.toList());
    }

    private LoanResponseDTO toResponseDTO(Loan loan) {
        LoanResponseDTO dto = new LoanResponseDTO();
        dto.setId(loan.getId());
        dto.setBookId(loan.getBookId());
        dto.setUserId(loan.getUserId());
        dto.setLoanDate(loan.getLoanDate());
        dto.setReturnDate(loan.getReturnDate());
        return dto;
    }

    private Loan toEntity(LoanRequestDTO dto) {
        Loan loan = new Loan();
        loan.setBookId(dto.getBookId());
        loan.setUserId(dto.getUserId());
        loan.setLoanDate(dto.getLoanDate());
        return loan;
    }

    private BookResponseDTO toBookResponseDTO(Book book) {
        return getBookResponseDTO(book);
    }

    static BookResponseDTO getBookResponseDTO(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setPublishedYear(book.getPublishedYear());
        dto.setIsbn(book.getIsbn());
        dto.setCopiesAvailable(book.getCopiesAvailable());
        return dto;
    }
}

