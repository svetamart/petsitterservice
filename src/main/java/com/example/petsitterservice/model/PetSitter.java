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
@Table(name = "pet_sitters")
public class PetSitter implements UserDetails {
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

    @Column(nullable = false)
    private String experience;

    @Column(nullable = false)
    private List<String> acceptedPets;

    @ElementCollection
    private List<String> acceptedDogSizes;

    @Column(nullable = false)
    private boolean hasOwnPets;

    @Column(nullable = false)
    private String bio;

    @Column(nullable = false)
    private double hourlyRate; // Часовая ставка

    @Column(nullable = false)
    private double dailyRate; // Суточная ставка

    @Column(nullable = false)
    private boolean takingNewOrders;
    @Column(nullable = false)
    private String phone;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();


    @ElementCollection
    @CollectionTable(name = "sitter_availability_dates", joinColumns = @JoinColumn(name = "sitter_id"))
    @Column(name = "availability_date", nullable = false)
    private List<String> availabilityDates;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PetBoardingRequest> requests = new ArrayList<>();

    @Column(nullable = false)
    private boolean accountEnabled;



    @Transient
    @JsonDeserialize(using = GrantedAuthorityDeserializer.class)
    public Collection<? extends GrantedAuthority> authorities;

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

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public List<String> getAcceptedPets() {
        return acceptedPets;
    }

    public void setAcceptedPets(List<String> acceptedPets) {
        this.acceptedPets = acceptedPets;
    }

    public List<String> getAcceptedDogSizes() {
        return acceptedDogSizes;
    }

    public void setAcceptedDogSizes(List<String> acceptedDogSizes) {
        this.acceptedDogSizes = acceptedDogSizes;
    }

    public boolean hasOwnPets() {
        return hasOwnPets;
    }

    public void setHasOwnPets(boolean hasOwnPets) {
        this.hasOwnPets = hasOwnPets;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<String> getAvailabilityDates() {
        return availabilityDates;
    }

    public void setAvailabilityDates(List<String> availabilityDates) {
        this.availabilityDates = availabilityDates;
    }

    @JsonIgnore
    public List<PetBoardingRequest> getRequests() {
        return requests;
    }

    public void addRequest(PetBoardingRequest request) {
        this.requests.add(request);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public boolean isTakingNewOrders() {
        return takingNewOrders;
    }

    public void setTakingNewOrders(boolean takingNewOrders) {
        this.takingNewOrders = takingNewOrders;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public boolean isAccountEnabled() {
        return accountEnabled;
    }

    public void setAccountEnabled(boolean accountEnabled) {
        this.accountEnabled = accountEnabled;
    }
}
