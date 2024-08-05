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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceImplTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    private Loan testLoan;
    private LoanRequestDTO testLoanRequestDTO;
    private Book testBook;
    private UUID testId;
    private UUID testUserId;
    private UUID testBookId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testId = UUID.randomUUID();
        testUserId = UUID.randomUUID();
        testBookId = UUID.randomUUID();

        testLoan = new Loan();
        testLoan.setId(testId);
        testLoan.setUserId(testUserId);
        testLoan.setBookId(testBookId);
        testLoan.setLoanDate(LocalDate.now());

        testLoanRequestDTO = new LoanRequestDTO();
        testLoanRequestDTO.setUserId(testUserId);
        testLoanRequestDTO.setBookId(testBookId);
        testLoanRequestDTO.setLoanDate(LocalDate.now());

        testBook = new Book();
        testBook.setId(testBookId);
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setPublishedYear(2021);
        testBook.setIsbn("1234567890");
        testBook.setCopiesAvailable(5);
    }

    @Test
    void getAllLoans_ShouldReturnListOfLoanResponseDTOs() {
        when(loanRepository.findAll()).thenReturn(Collections.singletonList(testLoan));

        List<LoanResponseDTO> result = loanService.getAllLoans();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(testLoan.getId(), result.get(0).getId());
    }

    @Test
    void getLoanById_WithValidId_ShouldReturnLoanResponseDTO() {
        when(loanRepository.findById(testId)).thenReturn(Optional.of(testLoan));

        LoanResponseDTO result = loanService.getLoanById(testId);

        assertNotNull(result);
        assertEquals(testLoan.getId(), result.getId());
    }

    @Test
    void getLoanById_WithInvalidId_ShouldThrowResourceNotFoundException() {
        when(loanRepository.findById(testId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> loanService.getLoanById(testId));
    }

    @Test
    void recordLoan_WithValidData_ShouldReturnLoanResponseDTO() {
        when(userRepository.existsById(testUserId)).thenReturn(true);
        when(bookRepository.existsById(testBookId)).thenReturn(true);
        when(loanRepository.save(any(Loan.class))).thenReturn(testLoan);

        LoanResponseDTO result = loanService.recordLoan(testLoanRequestDTO);

        assertNotNull(result);
        assertEquals(testLoan.getId(), result.getId());
    }

    @Test
    void recordLoan_WithInvalidUserId_ShouldThrowResourceNotFoundException() {
        when(userRepository.existsById(testUserId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> loanService.recordLoan(testLoanRequestDTO));
    }

    @Test
    void recordLoan_WithInvalidBookId_ShouldThrowResourceNotFoundException() {
        when(userRepository.existsById(testUserId)).thenReturn(true);
        when(bookRepository.existsById(testBookId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> loanService.recordLoan(testLoanRequestDTO));
    }

    @Test
    void returnLoan_WithValidIdAndDate_ShouldReturnUpdatedLoanResponseDTO() {
        LocalDate returnDate = LocalDate.now().plusDays(7);
        when(loanRepository.findById(testId)).thenReturn(Optional.of(testLoan));
        when(loanRepository.save(any(Loan.class))).thenReturn(testLoan);

        LoanResponseDTO result = loanService.returnLoan(testId, returnDate);

        assertNotNull(result);
        assertEquals(returnDate, result.getReturnDate());
    }

    @Test
    void returnLoan_WithInvalidId_ShouldThrowResourceNotFoundException() {
        when(loanRepository.findById(testId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> loanService.returnLoan(testId, LocalDate.now()));
    }

    @Test
    void returnLoan_WithInvalidDate_ShouldThrowIllegalArgumentException() {
        LocalDate invalidReturnDate = LocalDate.now().minusDays(1);
        when(loanRepository.findById(testId)).thenReturn(Optional.of(testLoan));

        assertThrows(IllegalArgumentException.class, () -> loanService.returnLoan(testId, invalidReturnDate));
    }

    @Test
    void getBooksLoanedByUser_ShouldReturnListOfBookResponseDTOs() {
        when(loanRepository.findByUserId(testUserId)).thenReturn(Collections.singletonList(testLoan));
        when(bookRepository.findById(testBookId)).thenReturn(Optional.of(testBook));

        List<BookResponseDTO> result = loanService.getBooksLoanedByUser(testUserId);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(testBook.getTitle(), result.get(0).getTitle());
    }

    @Test
    void getBooksLoanedByUser_WithInvalidUserId_ShouldReturnEmptyList() {
        when(loanRepository.findByUserId(testUserId)).thenReturn(List.of());

        List<BookResponseDTO> result = loanService.getBooksLoanedByUser(testUserId);

        assertTrue(result.isEmpty());
    }
}