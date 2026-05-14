package pe.edu.upc.travelmatch.experiences.application.internal.commandservices;

import java.util.Arrays;
import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.SeedTicketTypesCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.TicketType;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.TicketTypes;
import pe.edu.upc.travelmatch.experiences.domain.services.TicketTypeCommandService;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.TicketTypeRepository;

/** Service implementation for managing TicketType commands. */
@Service
public class TicketTypeCommandServiceImpl implements TicketTypeCommandService {

  private final TicketTypeRepository ticketTypeRepository;

  /**
   * Constructs a TicketTypeCommandServiceImpl.
   *
   * @param ticketTypeRepository the ticket type repository
   */
  public TicketTypeCommandServiceImpl(TicketTypeRepository ticketTypeRepository) {
    this.ticketTypeRepository = ticketTypeRepository;
  }

  @Override
  public void handle(SeedTicketTypesCommand command) {
    Arrays.stream(TicketTypes.values())
        .forEach(
            ticketType -> {
              if (!ticketTypeRepository.existsByName(ticketType)) {
                ticketTypeRepository.save(new TicketType(TicketTypes.valueOf(ticketType.name())));
              }
            });
  }
}
