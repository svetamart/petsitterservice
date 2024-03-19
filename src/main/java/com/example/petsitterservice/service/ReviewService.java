package com.example.petsitterservice.service;


import com.example.petsitterservice.model.Review;
import com.example.petsitterservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void addReview (Review review) {
        reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
