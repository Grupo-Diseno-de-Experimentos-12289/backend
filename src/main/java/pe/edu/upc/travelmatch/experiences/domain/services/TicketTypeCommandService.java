package pe.edu.upc.travelmatch.experiences.domain.services;

import pe.edu.upc.travelmatch.experiences.domain.model.commands.SeedTicketTypesCommand;

public interface TicketTypeCommandService {
    void handle(SeedTicketTypesCommand command);
}
