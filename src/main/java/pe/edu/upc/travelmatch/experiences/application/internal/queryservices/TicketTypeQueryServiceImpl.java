package pe.edu.upc.travelmatch.experiences.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.TicketType;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAllTicketTypesQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetTicketTypeByIdQuery;
import pe.edu.upc.travelmatch.experiences.domain.services.TicketTypeQueryService;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.TicketTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TicketTypeQueryServiceImpl implements TicketTypeQueryService {
    private final TicketTypeRepository ticketTypeRepository;

    public TicketTypeQueryServiceImpl(TicketTypeRepository ticketTypeRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
    }

    @Override
    public List<TicketType> handle(GetAllTicketTypesQuery query) {
        return ticketTypeRepository.findAll();
    }

    @Override
    public Optional<TicketType> handle(GetTicketTypeByIdQuery query) {
        return ticketTypeRepository.findById(query.ticketTypeId());
    }
}
