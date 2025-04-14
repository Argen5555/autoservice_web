package kg.autoservice.service;

import kg.autoservice.dto.ReviewDto;
import kg.autoservice.exception.ResourceNotFoundException;
import kg.autoservice.model.Review;
import kg.autoservice.model.Service;
import kg.autoservice.model.User;
import kg.autoservice.repository.ReviewRepository;
import kg.autoservice.repository.ServiceRepository;
import kg.autoservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;

    /**
     * Получение всех отзывов
     */
    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Получение отзыва по ID
     */
    public ReviewDto getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        return convertToDto(review);
    }

    /**
     * Получение всех отзывов пользователя
     */
    public List<ReviewDto> getReviewsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        return reviewRepository.findByUser(user).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Получение всех отзывов по услуге
     */
    public List<ReviewDto> getReviewsByService(Long serviceId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + serviceId));

        return reviewRepository.findByService(service).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Получение только утвержденных отзывов
     */
    public List<ReviewDto> getApprovedReviews() {
        return reviewRepository.findByIsApprovedTrue().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Получение только неутвержденных отзывов (для модерации)
     */
    public List<ReviewDto> getPendingReviews() {
        return reviewRepository.findByIsApprovedFalse().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Создание нового отзыва
     */
    @Transactional
    public ReviewDto createReview(ReviewDto reviewDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Service service = null;
        if (reviewDto.getServiceId() != null) {
            service = serviceRepository.findById(reviewDto.getServiceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + reviewDto.getServiceId()));
        }

        Review review = new Review();
        review.setUser(user);
        review.setService(service);
        review.setRating(reviewDto.getRating());
        review.setComment(reviewDto.getComment());
        review.setIsApproved(false); // По умолчанию отзыв не утвержден
        review.setCreatedAt(LocalDateTime.now());

        reviewRepository.save(review);

        return convertToDto(review);
    }

    /**
     * Утверждение или отклонение отзыва
     */
    @Transactional
    public ReviewDto approveReview(Long id, boolean approved) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));

        review.setIsApproved(approved);
        reviewRepository.save(review);

        return convertToDto(review);
    }

    /**
     * Удаление отзыва
     */
    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));

        reviewRepository.delete(review);
    }

    /**
     * Конвертация сущности в DTO
     */
    private ReviewDto convertToDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setUserId(review.getUser().getId());
        dto.setUserName(review.getUser().getFullName());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setIsApproved(review.getIsApproved());
        dto.setCreatedAt(review.getCreatedAt());

        if (review.getService() != null) {
            dto.setServiceId(review.getService().getId());
            dto.setServiceName(review.getService().getName());
        }

        return dto;
    }
}