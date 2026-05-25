package com.sf.auth.service;

import com.sf.auth.dto.LoginDTO;
import com.sf.auth.dto.RegisterDTO;

public interface UserAuthService {

    String login(LoginDTO loginDTO);

    String register(RegisterDTO registerDTO);

}
