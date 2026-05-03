package pe.edu.upc.travelmatch.experiences.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.shared.domain.model.entities.AuditableModel;

import java.math.BigDecimal;

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

    public AvailabilityTicketType(Availability availability, TicketType ticketType, BigDecimal price, int stock) {
        this.availability = availability;
        this.ticketType = ticketType;
        this.price = price;
        this.stock = stock;
    }

    public void reduceStock(int quantity) {
        if (stock < quantity) {
            throw new IllegalStateException("Not enough stock available.");
        }
        this.stock -= quantity;
    }
}