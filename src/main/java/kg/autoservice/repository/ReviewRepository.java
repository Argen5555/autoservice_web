package kg.autoservice.repository;


import kg.autoservice.model.Review;
import kg.autoservice.model.Service;
import kg.autoservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByUser(User user);

    List<Review> findByService(Service service);

    List<Review> findByIsApprovedTrue();

    List<Review> findByIsApprovedFalse();

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.service.id = ?1 AND r.isApproved = true")
    Double getAverageRatingForService(Long serviceId);
}