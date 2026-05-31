package com.awp.user.service;

import com.awp.auth.exception.userDomain.PhoneAlreadyExistsException;
import com.awp.auth.exception.userDomain.UserNotFoundException;
import com.awp.auth.model.User;
import com.awp.auth.repository.UserRepository;
import com.awp.user.dto.UserRequestDTO;
import com.awp.user.dto.UserResponseDTO;
import com.awp.user.dto.UserResponsePage;
import com.awp.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public UserResponsePage getAllUsers(int pageNo, int pageSize, String sortBy) {
        log.info("Request received to fetch all users from the system.");

        Sort sorted = Sort.by(sortBy).ascending();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sorted);

        Page<User> userPage = userRepository.findAll(pageRequest);

        List<UserResponseDTO> allUsers = userPage.getContent().stream().map(mapper::toResponse).toList();

        log.info("Successfully fetched {} user records from the database.", allUsers.size());

        return UserResponsePage.builder()
                .content(allUsers)
                .pageNo(userPage.getNumber())
                .pageSize(userPage.getSize())
                .totalPages(userPage.getTotalPages())
                .lastPage(userPage.isLast())
                .build();
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
        log.info("Processing secure profile update transaction for User ID: {}", id);

        return userRepository.findById(id).map(user -> {

            /*
            if (!user.getEmail().equalsIgnoreCase(request.email()) && userRepository.existsByEmail(request.email())) {
                log.warn("Update aborted: Email {} is already assigned to a different account.", request.email());
                throw new EmailAlreadyExistsException("This email is already claimed by another account!");
            }
            */

            // 2. UNIQUE PHONE CHECK
            if (!user.getPhone().equalsIgnoreCase(request.phone()) && userRepository.existsByPhone(request.phone())) {
                log.warn("Update aborted: Phone number {} is already assigned to a different account.", request.phone());
                throw new PhoneAlreadyExistsException("This phone number is already claimed by another account!");
            }
            user.setName(request.name());
//          user.setEmail(request.email());
            user.setPhone(request.phone());
            user.setAddress(request.address());

            /*
            if (request.password() != null && !request.password().isBlank()) {
                user.setPassword(passwordEncoder.encode(request.password()));
            }
            */

            User updatedUser = userRepository.save(user);
            log.info("Database state successfully synchronized for User ID: {}", id);
            return mapper.toResponse(updatedUser);

        }).orElseThrow(() -> {
            log.warn("Update operation failed: Identity record target ID {} does not exist.", id);
            return new UserNotFoundException("User with the given ID: " + id + " not found!");
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
