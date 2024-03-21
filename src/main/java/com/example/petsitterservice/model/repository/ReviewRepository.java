package com.example.petsitterservice.model.repository;

import com.example.petsitterservice.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
