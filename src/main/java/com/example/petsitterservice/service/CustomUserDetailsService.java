package com.example.petsitterservice.service;

import com.example.petsitterservice.model.PetOwner;
import com.example.petsitterservice.model.PetSitter;
import com.example.petsitterservice.model.repository.PetOwnerRepository;
import com.example.petsitterservice.model.repository.PetSitterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PetSitterRepository sitterRepository;
    private final PetOwnerRepository ownerRepository;

    @Autowired
    public CustomUserDetailsService(PetSitterRepository sitterRepository, PetOwnerRepository ownerRepository) {
        this.sitterRepository = sitterRepository;
        this.ownerRepository = ownerRepository;
    }

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
