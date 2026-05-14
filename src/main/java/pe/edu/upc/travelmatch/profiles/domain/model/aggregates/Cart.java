package pe.edu.upc.travelmatch.profiles.domain.model.aggregates;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.edu.upc.travelmatch.profiles.domain.model.entities.CartItem;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

/** Cart type. */
@Entity
@NoArgsConstructor
public class Cart extends AuditableAbstractAggregateRoot<Cart> {
  @Getter
  @Embedded
  @AttributeOverride(
      name = "userId",
      column = @Column(name = "user_id", nullable = false, unique = true))
  private UserId userId;

  @Getter
  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<CartItem> items = new ArrayList<>();

  /** Constructs a new Cart. */
  public Cart(UserId userId) {
    this.userId = userId;
  }

  /** Add item. */
  public void addItem(CartItem item) {
    boolean exists =
        items.stream()
            .anyMatch(existing -> existing.getAvailabilityId().equals(item.getAvailabilityId()));
    if (exists) {
      throw new IllegalStateException("This availability is already in the cart.");
    }
    item.setCart(this);
    this.items.add(item);
  }

  /** Remove item. */
  public void removeItem(AvailabilityId availabilityId) {
    CartItem itemToRemove =
        this.items.stream()
            .filter(item -> item.getAvailabilityId().equals(availabilityId))
            .findFirst()
            .orElseThrow(
                () -> new IllegalStateException("Item with this availability not found in cart."));

    itemToRemove.setCart(null);
    this.items.remove(itemToRemove);
  }
}
