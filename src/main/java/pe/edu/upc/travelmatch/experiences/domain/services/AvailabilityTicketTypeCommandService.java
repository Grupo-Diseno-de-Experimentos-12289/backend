package pe.edu.upc.travelmatch.experiences.domain.services;

import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateAvailabilityTicketTypeCommand;

public interface AvailabilityTicketTypeCommandService {
    Long handle(CreateAvailabilityTicketTypeCommand command);
}