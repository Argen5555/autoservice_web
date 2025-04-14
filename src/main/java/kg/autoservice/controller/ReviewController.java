package kg.autoservice.controller;


import kg.autoservice.dto.ReviewDto;
import kg.autoservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Получение всех утвержденных отзывов (публичный доступ)
     */
    @GetMapping("/approved")
    public ResponseEntity<List<ReviewDto>> getApprovedReviews() {
        List<ReviewDto> reviews = reviewService.getApprovedReviews();
        return ResponseEntity.ok(reviews);
    }

    /**
     * Получение всех отзывов пользователя
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByUser(@PathVariable Long userId) {
        List<ReviewDto> reviews = reviewService.getReviewsByUser(userId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Получение всех отзывов для услуги
     */
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByService(@PathVariable Long serviceId) {
        List<ReviewDto> reviews = reviewService.getReviewsByService(serviceId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Получение отзыва по ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {
        ReviewDto review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    /**
     * Создание нового отзыва
     */
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@Valid @RequestBody ReviewDto reviewDto,
                                                  Authentication authentication) {
        ReviewDto createdReview = reviewService.createReview(reviewDto, authentication.getName());
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    /**
     * Получение всех отзывов (только для админов)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        List<ReviewDto> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    /**
     * Получение всех неутвержденных отзывов (для модерации, только для админов)
     */
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReviewDto>> getPendingReviews() {
        List<ReviewDto> reviews = reviewService.getPendingReviews();
        return ResponseEntity.ok(reviews);
    }

    /**
     * Утверждение отзыва (только для админов)
     */
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReviewDto> approveReview(@PathVariable Long id) {
        ReviewDto approvedReview = reviewService.approveReview(id, true);
        return ResponseEntity.ok(approvedReview);
    }

    /**
     * Отклонение отзыва (только для админов)
     */
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReviewDto> rejectReview(@PathVariable Long id) {
        ReviewDto rejectedReview = reviewService.approveReview(id, false);
        return ResponseEntity.ok(rejectedReview);
    }

    /**
     * Удаление отзыва (только для админов)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}