package pe.edu.upc.travelmatch.experiences.application.internal.queryservices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.TicketType;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAllTicketTypesQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetTicketTypeByIdQuery;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.TicketTypeRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketTypeQueryServiceImplTest {

  @Mock private TicketTypeRepository repository;
  @InjectMocks private TicketTypeQueryServiceImpl service;

  @Test
  @DisplayName("handle(GetAll) should return list from repository (AAA)")
  void handle_GetAll_ShouldReturnList() {
    // Arrange
    var query = new GetAllTicketTypesQuery();
    var tt1 = mock(TicketType.class);
    var tt2 = mock(TicketType.class);
    when(repository.findAll()).thenReturn(java.util.List.of(tt1, tt2));
    // Act

    var act = service.handle(query);

    // Assert
    assertEquals(2, act.size());
    assertSame(tt1, act.get(0));
    assertSame(tt2, act.get(1));
    verify(repository).findAll();
    verifyNoMoreInteractions(repository);
  }

  @Test
  void handle_GetTicketTypeById_ShouldReturnOptional() {
    // Arrange
    var query = new GetTicketTypeByIdQuery(5L);
    var ticket1 = mock(TicketType.class);
    when(repository.findById(5L)).thenReturn(Optional.of(ticket1));

    // Act
    Optional<TicketType> act = service.handle(query);

    // Assert
    assertTrue(act.isPresent());
    assertSame(ticket1, act.get());
    verify(repository).findById(5L);
    verifyNoMoreInteractions(repository);
  }
}
