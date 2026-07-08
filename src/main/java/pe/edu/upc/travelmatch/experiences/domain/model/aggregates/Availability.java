package pe.edu.upc.travelmatch.experiences.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.AvailabilityTicketType;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.TicketType;
import pe.edu.upc.travelmatch.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/** Represents an Availability for an Experience. */
@Entity
public class Availability extends AuditableAbstractAggregateRoot<Availability> {

  @Getter
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "experience_id", nullable = false)
  private Experience experience;

  @Getter private LocalDateTime startDateTime;

  @Getter private LocalDateTime endDateTime;

  @Getter private int capacity;

  @Getter private Date deletedAt;

  @Getter
  @OneToMany(mappedBy = "availability", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<AvailabilityTicketType> ticketTypes;

  /** Default constructor for Availability. */
  public Availability() {
    this.ticketTypes = new HashSet<>();
  }

  /**
   * Constructs a new Availability.
   *
   * @param experience the associated experience
   * @param startDateTime the start time
   * @param endDateTime the end time
   * @param capacity the capacity limit
   */
  public Availability(
      Experience experience, LocalDateTime startDateTime, LocalDateTime endDateTime, int capacity) {
    this();
    this.experience = experience;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
    this.capacity = capacity;
  }

  /**
   * Adds a ticket type to this availability.
   *
   * @param ticketType the ticket type
   * @param price the price
   * @param stock the stock count
   */
  public void addTicketType(TicketType ticketType, BigDecimal price, int stock) {
    AvailabilityTicketType availabilityTicketType =
        new AvailabilityTicketType(this, ticketType, price, stock);
    this.ticketTypes.add(availabilityTicketType);
  }

  /**
   * Updates ticket type price and stock.
   *
   * @param ticketType the ticket type
   * @param newPrice the updated price
   * @param newStock the updated stock count
   */
  public void updateTicketTypePriceAndStock(
      TicketType ticketType, BigDecimal newPrice, int newStock) {
    this.ticketTypes.stream()
        .filter(at -> at.getTicketType().equals(ticketType))
        .findFirst()
        .ifPresentOrElse(
            at -> {
              at.setPrice(newPrice);
              at.setStock(newStock);
            },
            () -> {
              throw new IllegalStateException("TicketType not found for this Availability.");
            });
  }

  /**
   * Decrements the stock of a specific ticket type.
   *
   * @param ticketType the ticket type
   * @param quantity the quantity to reduce
   */
  public void decrementStock(TicketType ticketType, int quantity) {
    this.ticketTypes.stream()
        .filter(at -> at.getTicketType().equals(ticketType))
        .findFirst()
        .ifPresentOrElse(
            at -> at.reduceStock(quantity),
            () -> {
              throw new IllegalStateException("TicketType not found for this Availability.");
            });
  }

  /**
   * Updates availability info.
   *
   * @param startDateTime the new start time
   * @param endDateTime the new end time
   * @param capacity the new capacity
   */
  public void updateInfo(LocalDateTime startDateTime, LocalDateTime endDateTime, int capacity) {
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
    this.capacity = capacity;
  }

  /** Marks the availability as deleted. */
  public void markAsDeleted() {
    this.deletedAt = new Date();
  }
}
