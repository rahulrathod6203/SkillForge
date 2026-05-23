package com.sf.appUser.service;

import com.sf.appUser.dto.AppUserRequest;
import com.sf.appUser.dto.AppUserResponse;

import java.util.List;

public interface AppUserService {

    AppUserResponse createUser(AppUserRequest request);

    List<AppUserResponse> getAllUsers();

    AppUserResponse getUserById(Long id);

    AppUserResponse updateUser(Long id, AppUserRequest request);

    String deleteUserById(Long id);
}
