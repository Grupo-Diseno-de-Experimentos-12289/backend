package pe.edu.upc.travelmatch.profiles.infrastructure.persistence.jpa.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Favorite;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;

/** FavoriteRepository contract. */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
  /** Find by user id and experience id. */
  Optional<Favorite> findByUserIdAndExperienceId(UserId userId, ExperienceId experienceId);

  /** Find all by user id. */
  List<Favorite> findAllByUserId(UserId userId);

  /** Find all by experience id. */
  List<Favorite> findAllByExperienceId(ExperienceId experienceId);
}
