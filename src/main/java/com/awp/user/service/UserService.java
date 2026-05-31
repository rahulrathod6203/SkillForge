package com.awp.user.service;

import com.awp.user.dto.UserRequestDTO;
import com.awp.user.dto.UserResponseDTO;
import com.awp.user.dto.UserResponsePage;

import java.util.List;

public interface UserService {

    UserResponsePage getAllUsers(int pageNo, int pageSize, String sortBy);

    UserResponseDTO getUserById(Long id);

    UserResponseDTO updateUser(Long id, UserRequestDTO request);

    String deleteUserById(Long id);
}
