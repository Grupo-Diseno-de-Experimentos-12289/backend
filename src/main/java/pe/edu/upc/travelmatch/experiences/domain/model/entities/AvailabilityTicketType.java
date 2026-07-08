package pe.edu.upc.travelmatch.experiences.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.shared.domain.model.entities.AuditableModel;

import java.math.BigDecimal;

/** Entity representing the availability ticket type. */
@Entity
@Getter
@NoArgsConstructor
public class AvailabilityTicketType extends AuditableModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "availability_id", nullable = false)
  private Availability availability;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ticket_type_id", nullable = false)
  private TicketType ticketType;

  @Column(nullable = false, precision = 10, scale = 2)
  @Setter
  private BigDecimal price;

  @Column(nullable = false)
  @Setter
  private int stock;

  /**
   * Constructs an AvailabilityTicketType.
   *
   * @param availability the related availability
   * @param ticketType the related ticket type
   * @param price the price
   * @param stock the available stock
   */
  public AvailabilityTicketType(
      Availability availability, TicketType ticketType, BigDecimal price, int stock) {
    this.availability = availability;
    this.ticketType = ticketType;
    this.price = price;
    this.stock = stock;
  }

  /**
   * Reduces the stock by a given quantity.
   *
   * @param quantity the amount to reduce by
   */
  public void reduceStock(int quantity) {
    if (stock < quantity) {
      throw new IllegalStateException("Not enough stock available.");
    }
    this.stock -= quantity;
  }
}
