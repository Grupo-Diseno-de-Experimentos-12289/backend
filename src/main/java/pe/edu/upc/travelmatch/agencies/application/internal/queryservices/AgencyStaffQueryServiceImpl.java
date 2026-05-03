package pe.edu.upc.travelmatch.agencies.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyStaff;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAllAgencyStaffByAgencyIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.model.queries.GetAgencyStaffByIdQuery;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyStaffQueryService;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyRepository;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyStaffRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AgencyStaffQueryServiceImpl implements AgencyStaffQueryService {

    private final AgencyStaffRepository agencyStaffRepository;
    private final AgencyRepository agencyRepository;

    public AgencyStaffQueryServiceImpl(AgencyStaffRepository agencyStaffRepository, AgencyRepository agencyRepository) {
        this.agencyStaffRepository = agencyStaffRepository;
        this.agencyRepository = agencyRepository;
    }

    @Override
    public List<AgencyStaff> handle(GetAllAgencyStaffByAgencyIdQuery query) {

        if (!agencyRepository.existsById(query.agencyId())) {
            return Collections.emptyList();
        }
        return agencyStaffRepository.findByAgencyId(query.agencyId());
    }

    @Override
    public Optional<AgencyStaff> handle(GetAgencyStaffByIdQuery query) {
        return agencyStaffRepository.findById(query.staffId());
    }
}