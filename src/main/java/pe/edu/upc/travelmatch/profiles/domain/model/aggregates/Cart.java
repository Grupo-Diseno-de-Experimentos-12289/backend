package pe.edu.upc.travelmatch.profiles.domain.model.aggregates;

import pe.edu.upc.travelmatch.profiles.domain.model.entities.CartItem;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class Cart extends AuditableAbstractAggregateRoot<Cart> {
    @Getter
    @Embedded
    @AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false, unique = true))
    private UserId userId;

    @Getter
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<CartItem> items = new ArrayList<>();

    public Cart(UserId userId) {
        this.userId = userId;
    }

    public void addItem(CartItem item) {
        boolean exists = items.stream()
                .anyMatch(existing -> existing.getAvailabilityId().equals(item.getAvailabilityId()));
        if (exists) {
            throw new IllegalStateException("This availability is already in the cart.");
        }
        item.setCart(this);
        this.items.add(item);
    }

    public void removeItem(AvailabilityId availabilityId) {
        CartItem itemToRemove = this.items.stream()
                .filter(item -> item.getAvailabilityId().equals(availabilityId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Item with this availability not found in cart."));

        itemToRemove.setCart(null);
        this.items.remove(itemToRemove);
    }
}
