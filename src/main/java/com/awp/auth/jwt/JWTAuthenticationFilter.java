package com.awp.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    // 🚀 FIXED: Hard-coded explicit constructor handles compile-time resolution tracking perfectly!
    public JWTAuthenticationFilter(JWTTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Extract raw token string safely from Request Header
        String token = getTokenFromRequestHeader(request);

        // 2. 🛡️ SAFE GUARD: Check for null first using short-circuit evaluation
        if (token != null && !token.isBlank() && jwtTokenProvider.validateToken(token)) {

            // Extract core identifier identity bound within the payload claims
            String username = jwtTokenProvider.getUsername(token);

            // Fetch active user records directly from database persistence context
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Build out an authentic context token instance passing valid roles/authorities
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // Build standard web request contextual detail footprints
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Establish thread-bound authentication visibility boundary
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // 3. Always pass control back into the filter network execution deck
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequestHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7).trim();
        }
        return null;
    }
}