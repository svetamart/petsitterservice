package com.example.petsitterservice.service;

import com.example.petsitterservice.model.PetOwner;
import com.example.petsitterservice.model.PetSitter;
import com.example.petsitterservice.repository.PetOwnerRepository;
import com.example.petsitterservice.repository.PetSitterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PetSitterRepository sitterRepository;

    @Autowired
    private PetOwnerRepository ownerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PetSitter sitter = sitterRepository.findByUsername(username);

        if (sitter != null) {
            return sitter;
        }

        PetOwner owner = ownerRepository.findByUsername(username);

        if (owner != null) {
            return owner;
        }

        throw new UsernameNotFoundException("User " + username + " is not found.");
    }
}
