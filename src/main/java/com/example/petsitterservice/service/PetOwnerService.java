package com.example.petsitterservice.service;

import com.example.petsitterservice.model.PetOwner;
import com.example.petsitterservice.repository.PetOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetOwnerService {
    private final PetOwnerRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public PetOwnerService(PetOwnerRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void addUser(PetOwner user) {
        userRepository.save(user);
    }

    public PetOwner getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<PetOwner> getAllUsers() {
        return userRepository.findAll();
    }

    public PetOwner getUserByName(String username) {
        return userRepository.findByUsername(username);
    }

    public void register(PetOwner user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username is already taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
