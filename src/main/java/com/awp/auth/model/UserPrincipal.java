package com.awp.auth.model;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    // 💡 Keeping the reference encapsulation clean
    private final User user;

    public Long getId() {
        return user.getId();
    }

    public String getName() {
        return user.getName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 🚀 This is 100% correct! Converts your Set<Role> to Set<SimpleGrantedAuthority>
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase())) // UPPERCASE helps ensure matches
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Returning email as the username anchor
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Must be true so Spring doesn't block them
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Must be true so Spring doesn't block them
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Must be true so Spring doesn't block them
    }

    @Override
    public boolean isEnabled() {
        return user.getActive() != null && user.getActive(); // Safe null check
    }
}