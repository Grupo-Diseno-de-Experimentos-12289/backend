package pe.edu.upc.travelmatch.profiles.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.Query;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Cart;
import pe.edu.upc.travelmatch.profiles.domain.model.entities.CartItem;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserId(UserId userId);

    boolean existsByUserId(UserId userId);

    @Query("""
    SELECT ci FROM Cart c
    JOIN c.items ci
    WHERE c.userId = :userId AND ci.availabilityId = :availabilityId
    """)
    Optional<CartItem> findCartItemByUserIdAndAvailabilityId(UserId userId, AvailabilityId availabilityId);


    @Query("""
    SELECT COUNT(ci) FROM Cart c
    JOIN c.items ci
    WHERE c.userId = :userId
    """)
    int countCartItemsByUserId(UserId userId);
}