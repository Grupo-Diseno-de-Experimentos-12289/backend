package pe.edu.upc.travelmatch.experiences.domain.services;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.SeedTicketTypesCommand;
/**
 * Service to manage TicketType commands.
 */
public interface TicketTypeCommandService {
  /**
   * Handles the SeedTicketTypesCommand.
   *
   * @param command the command object
   */
  void handle(SeedTicketTypesCommand command);
}
