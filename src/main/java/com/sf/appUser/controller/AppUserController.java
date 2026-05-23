package com.sf.appUser.controller;

import com.sf.appUser.dto.AppUserRequest;
import com.sf.appUser.dto.AppUserResponse;
import com.sf.appUser.service.AppUserService;
import jakarta.persistence.PostUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/appUsers")
@RequiredArgsConstructor
@Slf4j
public class AppUserController {

    private final AppUserService appUserService;

    @PostMapping
    public ResponseEntity<AppUserResponse> createUser(@Valid @RequestBody AppUserRequest request) {

        log.info("AppUser is being registered...");
        AppUserResponse createdUser = appUserService.createUser(request);
        log.info("AppUser registered... {}", createdUser);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdUser.id()).toUri();

        return ResponseEntity.created(location).body(createdUser);
    }

    @GetMapping
    public ResponseEntity<List<AppUserResponse>> getAllUsers() {
        log.info("Fetching all users...");
        return ResponseEntity.ok(appUserService.getAllUsers());
    }


    @GetMapping("/{id}")
    public ResponseEntity<AppUserResponse> getUserById(@PathVariable Long id) {
        log.info("Fetching single user by id...");
        return ResponseEntity.ok(appUserService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUserResponse> updateUser(@PathVariable Long id, @RequestBody AppUserRequest request) {
        log.info("Updating existing single user by id...");
        return ResponseEntity.accepted().body(appUserService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        log.info("Deleting existing single user by id...");
        String deletedMessage = appUserService.deleteUserById(id);
        return ResponseEntity.ok().body(deletedMessage);
    }
}
