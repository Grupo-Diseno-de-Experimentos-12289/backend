package pe.edu.upc.travelmatch.profiles.infrastructure.persistence.jpa.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Cart;
import pe.edu.upc.travelmatch.profiles.domain.model.entities.CartItem;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;

/** CartRepository contract. */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

  /** Find by user id. */
  Optional<Cart> findByUserId(UserId userId);

  /** Exists by user id. */
  boolean existsByUserId(UserId userId);

  /** Documentation. */
  @Query(
      """
      SELECT ci FROM Cart c
      JOIN c.items ci
      WHERE c.userId = :userId AND ci.availabilityId = :availabilityId
      """)
  Optional<CartItem> findCartItemByUserIdAndAvailabilityId(
      UserId userId, AvailabilityId availabilityId);

  /** Documentation. */
  @Query(
      """
      SELECT COUNT(ci) FROM Cart c
      JOIN c.items ci
      WHERE c.userId = :userId
      """)
  int countCartItemsByUserId(UserId userId);
}
