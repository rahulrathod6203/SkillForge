package com.awp.auth.service;

import com.awp.auth.dto.LoginDTO;
import com.awp.auth.dto.RegisterDTO;

public interface UserAuthService {

    String login(LoginDTO loginDTO);

    String register(RegisterDTO registerDTO);

}
