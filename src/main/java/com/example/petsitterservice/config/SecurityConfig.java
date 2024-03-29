package com.example.petsitterservice.config;


import com.example.petsitterservice.service.CustomUserDetailsService;
import com.example.petsitterservice.utility.CustomAuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService,BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }


    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return new CustomAuthenticationManager(customUserDetailsService, bCryptPasswordEncoder);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers("/petOwner/dashboard/**").hasRole("OWNER")
                                .requestMatchers("/api/users/dashboard/**").permitAll()
                                .requestMatchers("/petSitter/dashboard/**").hasRole("SITTER")
                                .requestMatchers("/api/petSitters/dashboard/**").permitAll()
                                .requestMatchers("/main/**").permitAll()
                                .requestMatchers("/api/main/**").permitAll()
                                .requestMatchers("/images/**").permitAll()
                                .requestMatchers("/login/**").permitAll()
                                .requestMatchers("/api/registerOwner").permitAll()
                                .requestMatchers("/api/registerSitter").permitAll()
                                .requestMatchers("/api/login/**").permitAll()
                                .requestMatchers("/register/**").permitAll()
                                .requestMatchers("/api/users/**").permitAll()
                                .requestMatchers("/api/petSitters/**").permitAll()
                                .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/auth")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .httpBasic(withDefaults())
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer
                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));


        return http.build();
    }
}
