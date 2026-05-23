package com.sf.appUser.service;

import com.sf.appUser.dto.AppUserRequest;
import com.sf.appUser.dto.AppUserResponse;
import com.sf.appUser.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService{

    private  final AppUserRepository userRepository;

    @Override
    public AppUserResponse createUser(AppUserRequest request) {

        if(userRepository.existsByEmail(request.email())){
            throw new RuntimeException("Email already exists!");
        }



        return null;
    }

    @Override
    public List<AppUserResponse> getAllUsers() {
        return List.of();
    }

    @Override
    public AppUserResponse getUserById(Long id) {
        return null;
    }

    @Override
    public AppUserResponse updateUser(Long id, AppUserRequest request) {
        return null;
    }

    @Override
    public void deleteUserById(Long id) {

    }
}
