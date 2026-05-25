package com.awp.user.service;

import com.awp.user.dto.UserRequestDTO;
import com.awp.user.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(Long id);

    UserResponseDTO updateUser(Long id, UserRequestDTO request);

    String deleteUserById(Long id);
}
