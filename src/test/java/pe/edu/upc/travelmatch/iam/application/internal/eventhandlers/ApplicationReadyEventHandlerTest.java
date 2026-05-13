package pe.edu.upc.travelmatch.iam.application.internal.eventhandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import pe.edu.upc.travelmatch.iam.domain.model.commands.SeedRolesCommand;
import pe.edu.upc.travelmatch.iam.domain.services.RoleCommandService;

/**
 * Unit tests for {@link ApplicationReadyEventHandler}.
 *
 * <p>This class validates that the roles seeding command is triggered when the application ready
 * event is received.
 */
@ExtendWith(MockitoExtension.class)
class ApplicationReadyEventHandlerTest {

  @Mock private RoleCommandService roleCommandService;
  @Mock private ApplicationReadyEvent applicationReadyEvent;
  @Mock private ConfigurableApplicationContext applicationContext;
  @InjectMocks private ApplicationReadyEventHandler applicationReadyEventHandler;

  @Test
  @DisplayName("on should execute SeedRolesCommand")
  void on_ShouldExecuteSeedRolesCommand() {
    // Arrange
    when(applicationReadyEvent.getApplicationContext()).thenReturn(applicationContext);
    when(applicationContext.getId()).thenReturn("TestApplication");

    // Act
    applicationReadyEventHandler.on(applicationReadyEvent);

    // Assert
    verify(applicationReadyEvent).getApplicationContext();
    verify(applicationContext).getId();
    verify(roleCommandService).handle(any(SeedRolesCommand.class));
    verifyNoMoreInteractions(roleCommandService, applicationReadyEvent, applicationContext);
  }
}
