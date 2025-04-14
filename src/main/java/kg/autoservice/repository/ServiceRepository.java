package kg.autoservice.repository;

;
import kg.autoservice.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    List<Service> findByCategory(String category);

    List<Service> findByFeaturedTrue();

    List<Service> findByNameContainingIgnoreCase(String keyword);
}
