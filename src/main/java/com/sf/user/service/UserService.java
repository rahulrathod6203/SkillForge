package com.sf.user.service;

import com.sf.user.dto.UserRequestDTO;
import com.sf.user.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(Long id);

    UserResponseDTO updateUser(Long id, UserRequestDTO request);

    String deleteUserById(Long id);
}
