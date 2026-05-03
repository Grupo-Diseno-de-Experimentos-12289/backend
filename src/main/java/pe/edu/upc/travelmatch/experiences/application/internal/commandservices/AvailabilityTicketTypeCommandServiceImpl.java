package pe.edu.upc.travelmatch.experiences.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateAvailabilityTicketTypeCommand;
import pe.edu.upc.travelmatch.experiences.domain.services.AvailabilityTicketTypeCommandService;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.AvailabilityRepository;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.TicketTypeRepository;

@Service
public class AvailabilityTicketTypeCommandServiceImpl implements AvailabilityTicketTypeCommandService {

    private final TicketTypeRepository ticketTypeRepository;
    private final AvailabilityRepository availabilityRepository;

    public AvailabilityTicketTypeCommandServiceImpl(TicketTypeRepository ticketTypeRepository,
                                                    AvailabilityRepository availabilityRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
        this.availabilityRepository = availabilityRepository;
    }

    @Override
    public Long handle(CreateAvailabilityTicketTypeCommand command) {
        var availability = availabilityRepository.findById(command.availabilityId())
                .orElseThrow(() -> new IllegalArgumentException("Availability with ID " + command.availabilityId() + " does not exist."));

        var ticketType = ticketTypeRepository.findById(command.ticketTypeId())
                .orElseThrow(() -> new IllegalArgumentException("TicketType not found with ID: " + command.ticketTypeId()));

        availability.addTicketType(ticketType, command.price(), command.stock());
        availabilityRepository.save(availability);
        return availability.getId();
    }
}