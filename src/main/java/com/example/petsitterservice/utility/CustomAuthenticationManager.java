package com.example.petsitterservice.utility;


import com.example.petsitterservice.service.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class CustomAuthenticationManager implements AuthenticationManager {

    private final CustomUserDetailsService customUserDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationManager.class);

    public CustomAuthenticationManager(CustomUserDetailsService customUserDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails user = customUserDetailsService.loadUserByUsername(username);

        if (user != null && bCryptPasswordEncoder.matches(password, user.getPassword())) {
            logger.info("VERIFICATION PASSED");
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        } else {
            logger.info("VERIFICATION FAILED");
            throw new AuthenticationException("Authentication failed") {
            };
        }
    }
}
