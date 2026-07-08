package pe.edu.upc.travelmatch.experiences.interfaces.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.TicketType;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetAllTicketTypesQuery;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.TicketTypes;
import pe.edu.upc.travelmatch.experiences.domain.services.TicketTypeQueryService;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.TicketTypeResource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketTypesControllerTest {

  @Mock private TicketTypeQueryService ticketTypeQueryService;

  @InjectMocks private TicketTypesController ticketTypesController;

  @Test
  void testGetAllTicketTypes_Ok() {
    // Arrange
    TicketType ticketType = new TicketType(TicketTypes.TICKET_GENERAL);
    List<TicketType> ticketTypesList = List.of(ticketType);

    when(ticketTypeQueryService.handle(any(GetAllTicketTypesQuery.class)))
        .thenReturn(ticketTypesList);

    // Act
    ResponseEntity<List<TicketTypeResource>> response = ticketTypesController.getAllTicketTypes();

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    assertEquals("TICKET_GENERAL", response.getBody().get(0).name());
  }

  @Test
  void testGetAllTicketTypes_EmptyList() {
    // Arrange
    when(ticketTypeQueryService.handle(any(GetAllTicketTypesQuery.class))).thenReturn(List.of());

    // Act
    ResponseEntity<List<TicketTypeResource>> response = ticketTypesController.getAllTicketTypes();

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(0, response.getBody().size());
  }
}
