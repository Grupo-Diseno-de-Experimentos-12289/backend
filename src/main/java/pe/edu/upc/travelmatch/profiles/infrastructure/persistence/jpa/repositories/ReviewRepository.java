package pe.edu.upc.travelmatch.profiles.infrastructure.persistence.jpa.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Review;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;

/** ReviewRepository contract. */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
  /** Find by user id and experience id. */
  Optional<Review> findByUserIdAndExperienceId(UserId userId, ExperienceId experienceId);

  /** Find all by user id. */
  List<Review> findAllByUserId(UserId userId);

  /** Find all by experience id. */
  List<Review> findAllByExperienceId(ExperienceId experienceId);

  /** Delete by user id and experience id. */
  void deleteByUserIdAndExperienceId(UserId userId, ExperienceId experienceId);
}
