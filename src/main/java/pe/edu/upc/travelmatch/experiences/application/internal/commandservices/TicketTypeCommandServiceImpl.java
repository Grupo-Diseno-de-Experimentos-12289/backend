package pe.edu.upc.travelmatch.experiences.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.SeedTicketTypesCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.TicketType;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.TicketTypes;
import pe.edu.upc.travelmatch.experiences.domain.services.TicketTypeCommandService;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.TicketTypeRepository;

import java.util.Arrays;

@Service
public class TicketTypeCommandServiceImpl implements TicketTypeCommandService {
    private final TicketTypeRepository ticketTypeRepository;

    public TicketTypeCommandServiceImpl(TicketTypeRepository ticketTypeRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
    }

    @Override
    public void handle(SeedTicketTypesCommand command) {
        Arrays.stream(TicketTypes.values()).forEach(ticketType -> {
            if (!ticketTypeRepository.existsByName(ticketType)) {
                ticketTypeRepository.save(new TicketType(TicketTypes.valueOf(ticketType.name())));
            }
        });
    }
}
