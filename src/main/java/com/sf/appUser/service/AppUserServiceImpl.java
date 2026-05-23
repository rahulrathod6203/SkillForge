package com.sf.appUser.service;

import com.sf.appUser.dto.AppUserRequest;
import com.sf.appUser.dto.AppUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService{
    @Override
    public AppUserResponse createUser(AppUserRequest request) {
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
