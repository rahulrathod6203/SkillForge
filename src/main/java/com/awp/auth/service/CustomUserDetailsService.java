package com.awp.auth.service;

import com.awp.auth.exception.userDomain.UserNotFoundException;
import com.awp.auth.model.User;
import com.awp.auth.model.UserPrincipal;
import com.awp.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @NotNull
    @Override
    public UserDetails loadUserByUsername(@NotNull String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        UserPrincipal principal = new UserPrincipal(user);
        log.info("User authenticated successfully. Active authorities: {}", principal.getAuthorities());

        return principal;
    }
}
