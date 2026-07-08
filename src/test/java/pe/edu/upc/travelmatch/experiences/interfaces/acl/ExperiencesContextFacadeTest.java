package pe.edu.upc.travelmatch.experiences.interfaces.acl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAllExperiencesQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.AgencyId;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.Categories;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.DestinationId;
import pe.edu.upc.travelmatch.experiences.domain.services.AvailabilityQueryService;
import pe.edu.upc.travelmatch.experiences.domain.services.ExperienceQueryService;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.AvailabilityRepository;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.TicketTypeRepository;

@ExtendWith(MockitoExtension.class)
class ExperiencesContextFacadeTest {

    @Mock private ExperienceQueryService experienceQueryService;
    @Mock private AvailabilityQueryService availabilityQueryService;
    @Mock private AvailabilityRepository availabilityRepository;
    @Mock private TicketTypeRepository ticketTypeRepository;

    @InjectMocks private ExperiencesContextFacade experiencesContextFacade;

    @Test
    @DisplayName("fetchExperiencesByDestinationAndCategories filters by destination and category")
    void fetchExperiencesByDestinationAndCategories_filtersCorrectly() {
        // Arrange
        var experienceInDestination =
                new Experience(
                        "Tour Centro",
                        "Desc",
                        new AgencyId(1L),
                        new Category(Categories.CULTURA),
                        new DestinationId(1L),
                        "3h",
                        "Plaza Mayor");
        var experienceOtherDestination =
                new Experience(
                        "Tour Playa",
                        "Desc",
                        new AgencyId(1L),
                        new Category(Categories.NATURALEZA),
                        new DestinationId(2L),
                        "2h",
                        "Malecon");

        when(experienceQueryService.handle(any(GetAllExperiencesQuery.class)))
                .thenReturn(List.of(experienceInDestination, experienceOtherDestination));

        // Act
        var result =
                experiencesContextFacade.fetchExperiencesByDestinationAndCategories(1L, List.of("CULTURA"));

        // Assert
        assertEquals(1, result.size());
        assertEquals("Tour Centro", result.get(0).title());
    }

    @Test
    @DisplayName("fetchExperiencesByDestinationAndCategories returns all when filters are null/empty")
    void fetchExperiencesByDestinationAndCategories_noFilters_returnsAll() {
        // Arrange
        var experience =
                new Experience(
                        "Tour Libre",
                        "Desc",
                        new AgencyId(1L),
                        new Category(Categories.DEPORTE),
                        new DestinationId(3L),
                        "1h",
                        "Estadio");

        when(experienceQueryService.handle(any(GetAllExperiencesQuery.class)))
                .thenReturn(List.of(experience));

        // Act
        var result =
                experiencesContextFacade.fetchExperiencesByDestinationAndCategories(null, List.of());

        // Assert
        assertEquals(1, result.size());
    }
}