package com.casava.library.service.serviceImpl;

import com.casava.library.dto.request.UserRequestDTO;
import com.casava.library.dto.response.UserResponseDTO;
import com.casava.library.entity.User;
import com.casava.library.exception.ResourceAlreadyExistException;
import com.casava.library.exception.ResourceNotFoundException;
import com.casava.library.repository.UserRepository;
import com.casava.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.casava.library.constant.Constants.USER_NOT_FOUND_MESSAGE;
import static com.casava.library.util.Validator.validateRequest;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUserById(UUID id) {
        validateRequest(id);

        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE));
        return toResponseDTO(user);
    }

    @Override
    public UserResponseDTO addUser(UserRequestDTO userRequestDTO) {
        validateRequest(userRequestDTO);

        Optional.ofNullable(userRequestDTO.getEmail())
                .filter(email -> !userRepository.existsByEmail(email))
                .orElseThrow(() -> new ResourceAlreadyExistException("User with this email: " + userRequestDTO.getEmail() +
                        " already exists"));

        User user = toEntity(userRequestDTO);
        user = userRepository.save(user);
        return toResponseDTO(user);
    }

    @Override
    public UserResponseDTO updateUser(UUID id, UserRequestDTO userRequestDTO) {
        validateRequest(id);
        validateRequest(userRequestDTO);

        User existingUser = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE));
        existingUser.setName(userRequestDTO.getName());
        existingUser.setEmail(userRequestDTO.getEmail());
        existingUser.setMembershipDate(userRequestDTO.getMembershipDate());
        userRepository.save(existingUser);
        return toResponseDTO(existingUser);
    }

    @Override
    public void deleteUser(UUID id) {
        validateRequest(id);

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
        userRepository.deleteById(id);
    }

    private UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setMembershipDate(user.getMembershipDate());
        return dto;
    }

    private User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setMembershipDate(dto.getMembershipDate());
        return user;
    }
}
