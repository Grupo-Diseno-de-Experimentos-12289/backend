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

@Entity
public class Availability extends AuditableAbstractAggregateRoot<Availability> {

    @Getter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "experience_id", nullable = false)
    private Experience experience;

    @Getter
    private LocalDateTime startDateTime;

    @Getter
    private LocalDateTime endDateTime;

    @Getter
    private int capacity;

    @Getter
    private Date deletedAt;

    @Getter
    @OneToMany(mappedBy = "availability", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AvailabilityTicketType> ticketTypes;

    public Availability() {
        this.ticketTypes = new HashSet<>();
    }

    public Availability(Experience experience, LocalDateTime startDateTime, LocalDateTime endDateTime, int capacity) {
        this();
        this.experience = experience;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.capacity = capacity;
    }

    public void addTicketType(TicketType ticketType, BigDecimal price, int stock) {
        AvailabilityTicketType availabilityTicketType = new AvailabilityTicketType(this, ticketType, price, stock);
        this.ticketTypes.add(availabilityTicketType);
    }

    public void updateTicketTypePriceAndStock(TicketType ticketType, BigDecimal newPrice, int newStock) {
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


    public void updateInfo(LocalDateTime startDateTime, LocalDateTime endDateTime, int capacity) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.capacity = capacity;
    }

    public void markAsDeleted() {
        this.deletedAt = new Date();
    }
}