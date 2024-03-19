package com.example.petsitterservice.model;


import com.example.petsitterservice.utility.GrantedAuthorityDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "pet_owners")
public class PetOwner implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String role;

    @Transient
    @JsonDeserialize(using = GrantedAuthorityDeserializer.class)
    public Collection<? extends GrantedAuthority> authorities;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<PetBoardingRequest> requests = new ArrayList<>();

    @Column(nullable = false)
    private boolean accountEnabled;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PersonalRequest> personalRequests = new ArrayList<>();


    public PetOwner() {
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @JsonIgnore
    public List<PetBoardingRequest> getRequests() {
        return requests;
    }

    public void addRequest(PetBoardingRequest request) {
        this.requests.add(request);
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void addPet(Pet pet) {
        this.pets.add(pet);
    }


    public void setAccountEnabled(boolean accountEnabled) {
        this.accountEnabled = accountEnabled;
    }

    public boolean isAccountEnabled() {
        return accountEnabled;
    }

    public List<PersonalRequest> getPersonalRequests() {
        return personalRequests;
    }

    public void addPersonalRequest(PersonalRequest personalRequest) {
        this.personalRequests.add(personalRequest);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(getRole()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
