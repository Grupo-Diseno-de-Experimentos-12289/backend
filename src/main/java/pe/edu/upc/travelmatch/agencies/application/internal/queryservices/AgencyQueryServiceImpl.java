package pe.edu.upc.travelmatch.agencies.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAllAgenciesQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAgencyByIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyQueryService;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AgencyQueryServiceImpl implements AgencyQueryService {

    private final AgencyRepository agencyRepository;

    public AgencyQueryServiceImpl(AgencyRepository agencyRepository) {
        this.agencyRepository = agencyRepository;
    }

    @Override
    public Optional<Agency> handle(GetAgencyByIdQuery query) {
        return agencyRepository.findById(query.agencyId());
    }

    @Override
    public List<Agency> handle(GetAllAgenciesQuery query) {
        return agencyRepository.findAll();
    }
}