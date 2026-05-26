package com.awp.auth.service;

import com.awp.auth.dto.AuthResponse;
import com.awp.auth.dto.LoginDTO;
import com.awp.auth.dto.RegisterDTO;

public interface UserAuthService {

    AuthResponse login(LoginDTO loginDTO);

    AuthResponse register(RegisterDTO registerDTO);

}
