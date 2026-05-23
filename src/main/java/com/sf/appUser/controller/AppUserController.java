package com.sf.appUser.controller;

import com.sf.appUser.dto.AppUserRequest;
import com.sf.appUser.dto.AppUserResponse;
import com.sf.appUser.service.AppUserService;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/appUsers")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    @PostMapping
    public AppUserResponse createUser(AppUserRequest request) {
        return null;
    }

    @GetMapping
    public List<AppUserResponse> getAllUsers() {
        return List.of();
    }


    @GetMapping("/{id}")
    public AppUserResponse getUserById(Long id) {
        return null;
    }

    @PutMapping("/{id}")
    public AppUserResponse updateUser(Long id, AppUserRequest request) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(Long id) {

    }
}
