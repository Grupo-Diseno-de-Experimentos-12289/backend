package pe.edu.upc.travelmatch.experiences.application.internal.queryservices;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAllExperiencesQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetExperienceByIdQuery;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.ExperienceRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExperienceQueryServiceImplTest {

    @Mock private ExperienceRepository repository;
    @InjectMocks private ExperienceQueryServiceImpl service;

    @Test
    @DisplayName("handle(GetAllExperiencesQuery) should return list from repository (AAA)")
    void handle_GetAllExperiences_ShouldReturnList() {
        //Arrange
        var query = new GetAllExperiencesQuery();
        var exp1 = mock(Experience.class);
        var exp2 = mock(Experience.class);
        when(repository.findAll()).thenReturn(java.util.List.of(exp1, exp2));

        //Act
        var act = service.handle(query);

        //Assert
        assertEquals(2, act.size());
        assertSame(exp1, act.get(0));
        assertSame(exp2, act.get(1));
        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("handle(GetExperienceByIdQuery) should return Optional from repository (AAA)")
    void handle_GetExperienceById_ShouldReturnOptional() {
        //Arrange
        var query = new GetExperienceByIdQuery(5L);
        var exp = mock(Experience.class);
        when(repository.findById(5L)).thenReturn(java.util.Optional.of(exp));

        //Act
        var act = service.handle(query);

        //Assert
        assertTrue(act.isPresent());
        assertSame(exp, act.get());
        verify(repository).findById(5L);
        verifyNoMoreInteractions(repository);
    }

}
