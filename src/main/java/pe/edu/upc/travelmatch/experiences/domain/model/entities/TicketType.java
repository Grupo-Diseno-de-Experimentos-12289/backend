package pe.edu.upc.travelmatch.experiences.domain.model.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;
import lombok.Getter;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.TicketTypes;
import pe.edu.upc.travelmatch.shared.domain.model.entities.AuditableModel;
/**
 * Entity class for TicketType.
 */
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
  /**
   * Default constructor.
   */
  public TicketType() {
  }
  /**
   * Constructor with name.
   *
   * @param name the ticket type enum
   */
  public TicketType(TicketTypes name) {
    this.name = name;
  }
  /**
   * Get the ticket type name as string.
   *
   * @return the string name of the ticket type
   */
  public String getTicketTypeName() {
    return name.name();
  }
  /**
   * Gets the default ticket type.
   *
   * @return a default ticket type
   */
  public static TicketType getDefaultTicketType() {
    return new TicketType(TicketTypes.TICKET_GENERAL);
  }
  /**
   * Converts a string name to a TicketType.
   *
   * @param ticketType the string name
   * @return the corresponding TicketType
   */
  public static TicketType toTicketTypeFromName(String ticketType) {
    return new TicketType(TicketTypes.valueOf(ticketType));
  }
  /**
   * Validates a list of ticket types.
   *
   * @param ticketTypes the list to validate
   * @return the validated list or default if empty
   */
  public static List<TicketType> validateTicketTypeSet(List<TicketType> ticketTypes) {
    if (ticketTypes.isEmpty()) {
      return List.of(getDefaultTicketType());
    }
    return ticketTypes;
  }
}
