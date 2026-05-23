package com.sf.appUser.service;

import com.sf.appUser.dto.AppUserRequest;
import com.sf.appUser.dto.AppUserResponse;
import com.sf.appUser.exception.EmailAlreadyExistsException;
import com.sf.appUser.exception.UserNotFoundException;
import com.sf.appUser.mapper.AppUserMapper;
import com.sf.appUser.model.AppUser;
import com.sf.appUser.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository userRepository;
    private final AppUserMapper mapper;

    @Transactional
    @Override
    public AppUserResponse createUser(AppUserRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already exists!");
        }

        AppUser user = mapper.toEntity(request);
        user.setActive(true);
        AppUser savedUser = userRepository.save(user);
        return mapper.toResponse(savedUser);
    }

    @Override
    public List<AppUserResponse> getAllUsers() {
        List<AppUser> allAppUsers = userRepository.findAll();

        return allAppUsers.stream().map(mapper::toResponse).toList();
    }

    @Override
    public AppUserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("User with the given ID : " + id + " not found!"));
    }

    @Transactional
    @Override
    public AppUserResponse updateUser(Long id, AppUserRequest request) {
        return userRepository.findById(id).map(user -> {

            user.setFullName(request.fullName());
            user.setEmail(request.email());
            user.setPassword(request.password());
            user.setPhone(request.phone());
            user.setAddress(request.address());

            return mapper.toResponse(user);
        }).orElseThrow(() -> new UserNotFoundException("User with the given ID : " + id + " not found!"));
    }

    @Transactional
    @Override
    public String deleteUserById(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with the given ID : " + id + " not found!"));

        userRepository.deleteById(id);
        return "User with the given ID successfully deleted!";
    }
}
