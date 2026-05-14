package pe.edu.upc.travelmatch.profiles.domain.model.entities;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.travelmatch.profiles.domain.model.aggregates.Cart;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Money;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Quantity;
import pe.edu.upc.travelmatch.shared.domain.model.entities.AuditableModel;

/** CartItem type. */
@Entity
@NoArgsConstructor
public class CartItem extends AuditableModel {
  @Id
  @Getter
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  @Getter
  @AttributeOverrides({
    @AttributeOverride(
        name = "availabilityId",
        column = @Column(name = "availability_id", nullable = false))
  })
  private AvailabilityId availabilityId;

  @Embedded
  @Getter
  @Setter
  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "quantity", nullable = false))
  })
  private Quantity quantity;

  @Embedded
  @Getter
  @AttributeOverrides({
    @AttributeOverride(name = "amount", column = @Column(name = "price_amount", nullable = false)),
    @AttributeOverride(
        name = "currency",
        column = @Column(name = "price_currency", nullable = false))
  })
  private Money price;

  @ManyToOne(fetch = FetchType.LAZY)
  @Getter
  @Setter
  private Cart cart;

  /** Constructs a new CartItem. */
  public CartItem(AvailabilityId availabilityId, Quantity quantity, Money price) {
    this.availabilityId = availabilityId;
    this.quantity = quantity;
    this.price = price;
  }
}
