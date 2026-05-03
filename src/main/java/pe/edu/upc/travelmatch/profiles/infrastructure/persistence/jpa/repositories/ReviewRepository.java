package pe.edu.upc.travelmatch.profiles.infrastructure.persistence.jpa.repositories;

import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Review;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUserIdAndExperienceId(UserId userId, ExperienceId experienceId);
    List<Review> findAllByUserId(UserId userId);
    List<Review> findAllByExperienceId(ExperienceId experienceId);
    void deleteByUserIdAndExperienceId(UserId userId, ExperienceId experienceId);
}