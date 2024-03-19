package com.example.petsitterservice.repository;

import com.example.petsitterservice.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
