package pe.edu.upc.travelmatch.agencies.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyDocument;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAllAgencyDocumentsByAgencyIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAgencyDocumentByIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyDocumentQueryService;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyRepository;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyDocumentRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AgencyDocumentQueryServiceImpl implements AgencyDocumentQueryService {

    private final AgencyDocumentRepository agencyDocumentRepository;
    private final AgencyRepository agencyRepository;

    public AgencyDocumentQueryServiceImpl(AgencyDocumentRepository agencyDocumentRepository, AgencyRepository agencyRepository) {
        this.agencyDocumentRepository = agencyDocumentRepository;
        this.agencyRepository = agencyRepository;
    }

    @Override
    public List<AgencyDocument> handle(GetAllAgencyDocumentsByAgencyIdQuery query) {
        if (!agencyRepository.existsById(query.agencyId())) {
            return Collections.emptyList();
        }
        return agencyDocumentRepository.findByAgencyId(query.agencyId());
    }

    @Override
    public Optional<AgencyDocument> handle(GetAgencyDocumentByIdQuery query) {
        return agencyDocumentRepository.findById(query.documentId());
    }
}