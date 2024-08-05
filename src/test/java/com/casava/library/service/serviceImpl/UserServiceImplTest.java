package com.casava.library.service.serviceImpl;

import com.casava.library.dto.request.UserRequestDTO;
import com.casava.library.dto.response.UserResponseDTO;
import com.casava.library.entity.User;
import com.casava.library.exception.ResourceAlreadyExistException;
import com.casava.library.exception.ResourceNotFoundException;
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

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserRequestDTO testUserRequestDTO;
    private UUID testId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testId = UUID.randomUUID();
        testUser = new User();
        testUser.setId(testId);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setMembershipDate(LocalDate.now());

        testUserRequestDTO = new UserRequestDTO();
        testUserRequestDTO.setName("Test User");
        testUserRequestDTO.setEmail("test@example.com");
        testUserRequestDTO.setMembershipDate(LocalDate.now());
    }

    @Test
    void getAllUsers_ShouldReturnListOfUserResponseDTOs() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(testUser));

        List<UserResponseDTO> result = userService.getAllUsers();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(testUser.getName(), result.get(0).getName());
    }

    @Test
    void getUserById_WithValidId_ShouldReturnUserResponseDTO() {
        when(userRepository.findById(testId)).thenReturn(Optional.of(testUser));

        UserResponseDTO result = userService.getUserById(testId);

        assertNotNull(result);
        assertEquals(testUser.getName(), result.getName());
    }

    @Test
    void getUserById_WithInvalidId_ShouldThrowResourceNotFoundException() {
        when(userRepository.findById(testId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(testId));
    }

    @Test
    void addUser_WithValidData_ShouldReturnUserResponseDTO() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponseDTO result = userService.addUser(testUserRequestDTO);

        assertNotNull(result);
        assertEquals(testUserRequestDTO.getName(), result.getName());
    }

    @Test
    void addUser_WithExistingEmail_ShouldThrowResourceAlreadyExistException() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(ResourceAlreadyExistException.class, () -> userService.addUser(testUserRequestDTO));
    }

    @Test
    void updateUser_WithValidIdAndData_ShouldReturnUpdatedUserResponseDTO() {
        when(userRepository.findById(testId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponseDTO result = userService.updateUser(testId, testUserRequestDTO);

        assertNotNull(result);
        assertEquals(testUserRequestDTO.getName(), result.getName());
    }

    @Test
    void updateUser_WithInvalidId_ShouldThrowResourceNotFoundException() {
        when(userRepository.findById(testId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(testId, testUserRequestDTO));
    }

    @Test
    void deleteUser_WithValidId_ShouldDeleteUser() {
        when(userRepository.existsById(testId)).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser(testId));
        verify(userRepository, times(1)).deleteById(testId);
    }

    @Test
    void deleteUser_WithInvalidId_ShouldThrowResourceNotFoundException() {
        when(userRepository.existsById(testId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(testId));
    }
}