package pe.edu.upc.travelmatch.experiences.domain.services;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateAvailabilityTicketTypeCommand;
/**
 * Service to manage AvailabilityTicketType commands.
 */
public interface AvailabilityTicketTypeCommandService {
  /**
   * Handles the CreateAvailabilityTicketTypeCommand.
   *
   * @param command the command object
   * @return the availability ID
   */
  Long handle(CreateAvailabilityTicketTypeCommand command);
}
