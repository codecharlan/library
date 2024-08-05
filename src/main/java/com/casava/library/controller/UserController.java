package com.casava.library.controller;

import com.casava.library.advice.InternalCode;
import com.casava.library.dto.request.UserRequestDTO;
import com.casava.library.dto.response.UserResponseDTO;
import com.casava.library.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.casava.library.dto.response.ApiResponseDTO;
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
@RequestMapping("/users")
@Tag(name = "User", description = "User management APIs")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves a list of all users")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        ApiResponseDTO<List<UserResponseDTO>> response = new ApiResponseDTO<>(true,
                SUCCESS_MESSAGE, InternalCode.CASAVA_LIB_001, users);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by ID", description = "Retrieves a user by their ID")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserById(
            @Parameter(description = "ID of the user to retrieve") @PathVariable UUID id) {
        UserResponseDTO user = userService.getUserById(id);
        ApiResponseDTO<UserResponseDTO> response = new ApiResponseDTO<>(true,
                SUCCESS_MESSAGE, InternalCode.CASAVA_LIB_001, user);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Add a new user", description = "Creates a new user")
    @ApiResponse(responseCode = "201", description = "User created successfully",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> addUser(
            @Parameter(description = "User details") @RequestBody @Valid UserRequestDTO userRequestDTO) {
        UserResponseDTO user = userService.addUser(userRequestDTO);
        ApiResponseDTO<UserResponseDTO> response = new ApiResponseDTO<>(true,
                SUCCESS_MESSAGE, InternalCode.CASAVA_LIB_001, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user", description = "Updates an existing user")
    @ApiResponse(responseCode = "200", description = "User updated successfully",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> updateUser(
            @Parameter(description = "ID of the user to update") @PathVariable UUID id,
            @Parameter(description = "Updated user details") @RequestBody @Valid UserRequestDTO userRequestDTO) {
        UserResponseDTO user = userService.updateUser(id, userRequestDTO);
        ApiResponseDTO<UserResponseDTO> response = new ApiResponseDTO<>(true,
                SUCCESS_MESSAGE, InternalCode.CASAVA_LIB_001, user);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Deletes a user")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete") @PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}