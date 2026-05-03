package pe.edu.upc.travelmatch.experiences.interfaces.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.experiences.application.internal.queryservices.AvailabilityQueryServiceImpl;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.AvailabilityTicketType;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAvailabilityByIdQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetExperienceByIdQuery;
import pe.edu.upc.travelmatch.experiences.domain.services.AvailabilityQueryService;
import pe.edu.upc.travelmatch.experiences.domain.services.ExperienceQueryService;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.AvailabilityRepository;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.TicketTypeRepository;
import pe.edu.upc.travelmatch.experiences.interfaces.acl.dto.AvailabilityInfo;
import pe.edu.upc.travelmatch.experiences.interfaces.acl.dto.AvailabilityTicketTypeInfo;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ExperiencesContextFacade {

    private final ExperienceQueryService experienceQueryService;
    private final AvailabilityQueryService availabilityQueryService;
    private final AvailabilityRepository availabilityRepository;
    private final TicketTypeRepository ticketTypeRepository;

    public ExperiencesContextFacade(
            ExperienceQueryService experienceQueryService,
            AvailabilityQueryService availabilityQueryService,
            AvailabilityRepository availabilityRepository,
            TicketTypeRepository ticketTypeRepository
    ) {
        this.experienceQueryService = experienceQueryService;
        this.availabilityQueryService = availabilityQueryService;
        this.availabilityRepository = availabilityRepository;
        this.ticketTypeRepository = ticketTypeRepository;
    }

    public Optional<Experience> fetchExperienceById(Long experienceId) {
        return experienceQueryService.handle(new GetExperienceByIdQuery(experienceId));
    }

    public boolean existsExperienceById(Long experienceId) {
        return fetchExperienceById(experienceId).isPresent();
    }

    public boolean isExperienceOwnedByAgency(Long experienceId, Long agencyId) {
        var experience = fetchExperienceById(experienceId);
        return experience.isPresent() && experience.get().getAgencyId().equals(agencyId);
    }

    public Optional<AvailabilityInfo> fetchAvailabilityInfo(Long availabilityId) {
        var availabilityOpt = availabilityQueryService.handle(new GetAvailabilityByIdQuery(availabilityId));

        return availabilityOpt.map(availability -> {
            var ticketTypesInfo = availability.getTicketTypes().stream()
                    .map(att -> new AvailabilityTicketTypeInfo(
                            att.getTicketType().getId(),
                            att.getTicketType().getTicketTypeName(),
                            att.getPrice(),
                            att.getStock()
                    )).toList();

            return new AvailabilityInfo(
                    availability.getId(),
                    availability.getExperience().getId(),
                    availability.getStartDateTime(),
                    availability.getEndDateTime(),
                    ticketTypesInfo
            );
        });
    }

    public void decrementStock(Long availabilityId, Long ticketTypeId, int quantity) {
        var availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new IllegalArgumentException("Availability not found"));

        var ticketType = ticketTypeRepository.findById(ticketTypeId)
                .orElseThrow(() -> new IllegalArgumentException("TicketType not found"));

        availability.decrementStock(ticketType, quantity);
        availabilityRepository.save(availability);
    }

    public Optional<BigDecimal> fetchTicketPrice(Long availabilityId, Long ticketTypeId) {
        var availabilityOpt = availabilityQueryService.handle(new GetAvailabilityByIdQuery(availabilityId));

        return availabilityOpt.flatMap(availability -> availability.getTicketTypes().stream()
                .filter(at -> at.getTicketType().getId().equals(ticketTypeId))
                .map(AvailabilityTicketType::getPrice)
                .findFirst());

    }

    public boolean isStockAvailable(Long availabilityId, Long ticketTypeId, int requestedQuantity) {
        var availabilityOpt = availabilityQueryService.handle(new GetAvailabilityByIdQuery(availabilityId));

        return availabilityOpt.map(availability -> availability.getTicketTypes().stream()
                .filter(at -> at.getTicketType().getId().equals(ticketTypeId))
                .anyMatch(at -> at.getStock() >= requestedQuantity)).orElse(false);

    }

}