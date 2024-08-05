package com.casava.library.service;

import com.casava.library.dto.request.UserRequestDTO;
import com.casava.library.dto.response.UserResponseDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(UUID id);
    UserResponseDTO addUser(UserRequestDTO userRequestDTO);
    UserResponseDTO updateUser(UUID id, UserRequestDTO userRequestDTO);
    void deleteUser(UUID id);
}
