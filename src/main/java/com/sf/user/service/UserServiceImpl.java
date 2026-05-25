package com.sf.user.service;

import com.sf.auth.exception.EmailAlreadyExistsException;
import com.sf.auth.exception.PhoneAlreadyExistsException;
import com.sf.auth.model.User;
import com.sf.auth.repository.UserRepository;
import com.sf.user.dto.UserRequestDTO;
import com.sf.user.dto.UserResponseDTO;
import com.sf.auth.exception.UserNotFoundException;
import com.sf.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponseDTO> getAllUsers() {
        log.info("Request received to fetch all users from the system.");

        List<User> allUsers = userRepository.findAll();
        log.info("Successfully fetched {} user records from the database.", allUsers.size());

        return allUsers.stream().map(mapper::toResponse).toList();
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        log.info("Attempting to fetch details for User ID: {}", id);
        return userRepository.findById(id)
                .map(user -> {
                    log.info("Successfully found profile details for User ID: {}", id);
                    return mapper.toResponse(user);
                })
                .orElseThrow(() -> {
                    log.warn("Fetch failed: User with the given ID : {} not found!", id);
                    return new UserNotFoundException("User with the given ID : " + id + " not found!");
                });
    }

    @Transactional
    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO request) {
        log.info("Processing profile update request for User ID: {}", id);
        return userRepository.findById(id).map(user -> {
            if (!user.getEmail().equalsIgnoreCase(request.email()) && userRepository.existsByEmail(request.email())) {
                log.warn("Update rejected: Email {} is already claimed by another account.", request.email());
                throw new EmailAlreadyExistsException("This email is already claimed by another account!");
            }

            if (!user.getPhone().equalsIgnoreCase(request.phone()) && userRepository.existsByPhone(request.phone())) {
                log.warn("Update rejected: Phone number {} is already claimed by another account.", request.phone());
                throw new PhoneAlreadyExistsException("This phone number is already claimed by another account!");
            }

            user.setName(request.name());
            user.setEmail(request.email());
            user.setPhone(request.phone());
            user.setAddress(request.address());

            if (request.password() != null && !request.password().isBlank()) {
                user.setPassword(passwordEncoder.encode(request.password()));
            }
            User updatedUser = userRepository.save(user);
            log.info("Profile fields successfully modified and persisted for User ID: {}", id);
            return mapper.toResponse(updatedUser);
        }).orElseThrow(() -> {
            log.warn("Update failed: User with the given ID : {} not found!", id);
            return new UserNotFoundException("User with the given ID : " + id + " not found!");
        });
    }

    @Transactional
    @Override
    public String deleteUserById(Long id) {
        log.info("Attempting to delete user record from system for User ID: {}", id);

        userRepository.findById(id).orElseThrow(() -> {
            log.warn("Delete failed: User with the given ID : {} not found!", id);
            return new UserNotFoundException("User with the given ID : " + id + " not found!");
        });
        userRepository.deleteById(id);
        log.info("User with the given ID: {} successfully deleted from database.", id);

        return "User with the given ID successfully deleted!";
    }
}
