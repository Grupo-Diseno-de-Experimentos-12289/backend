package pe.edu.upc.travelmatch.experiences.application.internal.queryServices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.experiences.application.internal.queryservices.ExperienceMediaQueryServiceImpl;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.ExperienceMedia;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAllExperiencesQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetExperienceByIdQuery;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.ExperienceMediaRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExperienceMediaQueryServiceImplTest {

    @Mock private ExperienceMediaRepository repository;

    @InjectMocks private ExperienceMediaQueryServiceImpl service;

    @Test
    @DisplayName("handle(GetAll) should return list from repository (AAA)")
    void handle_GetAll_ShouldReturnList() {
        //Arrange
        var query = new GetAllExperiencesQuery();
        var exp1 = mock(ExperienceMedia.class);
        var exp2 = mock(ExperienceMedia.class);
        when(repository.findAll()).thenReturn(List.of(exp1, exp2));

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
    @DisplayName("handle(GetByExperienceId) should return list from repository (AAA)")
    void handle_GetByExperienceId_ShouldReturnList() {
        //Arrange
        var query = new GetExperienceByIdQuery(6L);
        var exp1 = mock(ExperienceMedia.class);
        var exp2 = mock(ExperienceMedia.class);
        when(repository.findByExperienceId(5L)).thenReturn(List.of(exp1, exp2));

        //Act
        Optional<ExperienceMedia> act = service.handle(query);

        //Assert
        assertTrue(act.isPresent());
        assertSame(exp1, act.get());
        verify(repository).findByExperienceId(5L);
        verifyNoMoreInteractions(repository);

    }
}
