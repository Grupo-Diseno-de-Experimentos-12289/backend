package pe.edu.upc.travelmatch.experiences.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.TicketTypes;
import pe.edu.upc.travelmatch.shared.domain.model.entities.AuditableModel;

import java.util.List;

@Entity
public class TicketType extends AuditableModel {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 30)
    private TicketTypes name;

    public TicketType() {}

    public TicketType(TicketTypes name) {
        this.name = name;
    }

    public String getTicketTypeName() {
        return name.name();
    }

    public static TicketType getDefaultTicketType() {
        return new TicketType(TicketTypes.TICKET_GENERAL);
    }

    public static TicketType toTicketTypeFromName(String ticketType) {
        return new TicketType(TicketTypes.valueOf(ticketType));
    }

    public static List<TicketType> validateTicketTypeSet(List<TicketType> ticketTypes) {
        if(ticketTypes.isEmpty()) {
            return List.of(getDefaultTicketType());
        }
        return ticketTypes;
    }
}
