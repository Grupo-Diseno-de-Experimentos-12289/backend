package pe.edu.upc.travelmatch.profiles.infrastructure.persistence.jpa.repositories;

import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Favorite;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUserIdAndExperienceId(UserId userId, ExperienceId experienceId);
    List<Favorite> findAllByUserId(UserId userId);
    List<Favorite> findAllByExperienceId(ExperienceId experienceId);
}