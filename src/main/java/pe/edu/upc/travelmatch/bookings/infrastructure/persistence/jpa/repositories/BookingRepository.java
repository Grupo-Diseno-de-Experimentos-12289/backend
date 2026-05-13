package pe.edu.upc.travelmatch.bookings.infrastructure.persistence.jpa.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.travelmatch.bookings.domain.model.aggregates.Booking;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.UserId;

/** BookingRepository contract. */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
  /** Find all by user id. */
  List<Booking> findAllByUserId(UserId userId);
}
