package com.awp.auth.service;

import com.awp.auth.dto.LoginDTO;
import com.awp.auth.dto.RegisterDTO;
import com.awp.auth.exception.EmailAlreadyExistsException;
import com.awp.auth.exception.PhoneAlreadyExistsException;
import com.awp.auth.jwt.JWTTokenProvider;
import com.awp.auth.mapper.AuthUserMapper;
import com.awp.auth.model.Role;
import com.awp.auth.model.User;
import com.awp.auth.repository.RoleRepository;
import com.awp.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthServiceImpl implements UserAuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthUserMapper mapper;
    private final RoleRepository roleRepository;
    private final JWTTokenProvider jwtTokenProvider;

    @Override
    public String login(LoginDTO loginDTO) {
        log.info("Attempting authentication processing for login email: {}", loginDTO.email());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Authentication manager verification successful. SecurityContext updated for email: {}", loginDTO.email());


       // return jwtTokenProvider.generateToken(authentication);

        return "Logged-in Successfully";
    }

    @Override
    public String register(RegisterDTO registerDTO) {
        log.info("Processing account registration request for email: {}", registerDTO.email());

        if (userRepository.existsByEmail(registerDTO.email())) {
            log.warn("Registration rejected: Email conflict discovered for address: {}", registerDTO.email());
            throw new EmailAlreadyExistsException("Email already exits, please try a different one!");
        }

        if (userRepository.existsByPhone(registerDTO.phone())) {
            log.warn("Registration rejected: Phone conflict discovered for number: {}", registerDTO.phone());
            throw new PhoneAlreadyExistsException("Oops! This phone number is already used by someone!, please try a different one!");
        }

        User user = mapper.toEntity(registerDTO);
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(registerDTO.password()));

        Set<Role> roles = new HashSet<>();
        Role defaultRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> {
                    log.error("CRITICAL CONFIGURATION ERROR: 'USER' baseline row missing inside the roles master table.");
                    return new RuntimeException("System Error: Default role 'USER' does not exist in database!");
                });

        roles.add(defaultRole);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        log.info("User identity record persisted successfully. Assigned Generated ID: {} with role: USER", savedUser.getId());

        return "Registered Successfully!";
    }
}