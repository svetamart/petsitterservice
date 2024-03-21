package com.example.petsitterservice.service;


import com.example.petsitterservice.model.Review;
import com.example.petsitterservice.model.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис, отвечающий за операции с отзывами.
 */
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    /**
     * Конструктор для создания экземпляра класса.
     *
     * @param reviewRepository   Репозиторий отзывов
     */
    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Метод для добавления нового отзыва.
     * @param review Объект отзыва, который нужно добавить.
     */
    public void addReview (Review review) {
        reviewRepository.save(review);
    }

    /**
     * Метод для удаления отзыва по его идентификатору.
     * @param id Идентификатор отзыва, который нужно удалить.
     */
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
