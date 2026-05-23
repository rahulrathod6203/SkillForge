package com.sf.appUser.service;

import com.sf.appUser.dto.AppUserRequest;
import com.sf.appUser.dto.AppUserResponse;
import com.sf.appUser.mapper.AppUserMapper;
import com.sf.appUser.model.AppUser;
import com.sf.appUser.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository userRepository;

    private final AppUserMapper mapper;

    @Override
    public AppUserResponse createUser(AppUserRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists!");
        }

        AppUser user = mapper.toEntity(request);
        user.setActive(true);
        AppUser savedUser = userRepository.save(user);
        return mapper.toResponse(savedUser);
    }

    @Override
    public List<AppUserResponse> getAllUsers() {
        List<AppUser> allAppUsers = userRepository.findAll();
        if (allAppUsers.isEmpty()) {
            throw new RuntimeException("No users registered yet!");
        }
        return allAppUsers.stream().map(mapper::toResponse).toList();
    }

    @Override
    public AppUserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("User with the given ID : " + id + " not found!"));
    }

    @Override
    public AppUserResponse updateUser(Long id, AppUserRequest request) {
        return userRepository.findById(id)
                .map(user -> {

                    AppUser.builder()
                            .fullName(request.fullName())
                            .email(request.email())
                            .password(request.password())
                            .phone(request.phone())
                            .address(request.address())
                            .build();

                    return mapper.toResponse(user);
                }).orElseThrow(() -> new RuntimeException("User with the given ID : " + id + " not found!"));
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with the given ID : " + id + " not found!"));

        userRepository.deleteById(id);
    }
}
