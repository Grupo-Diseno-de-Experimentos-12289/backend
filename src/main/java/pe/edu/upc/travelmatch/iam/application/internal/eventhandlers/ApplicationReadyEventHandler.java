package pe.edu.upc.travelmatch.iam.application.internal.eventhandlers;

import java.sql.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.iam.domain.model.commands.SeedRolesCommand;
import pe.edu.upc.travelmatch.iam.domain.services.RoleCommandService;

/**
 * Handles application-ready events and seeds roles when needed.
 */
@Service
public final class ApplicationReadyEventHandler {
  private final Logger logger = LoggerFactory.getLogger(ApplicationReadyEventHandler.class);

  private final RoleCommandService roleCommandService;

  /**
   * Constructor.
   *
   * @param roleCommandServiceDependency role command service dependency
   */
  public ApplicationReadyEventHandler(final RoleCommandService roleCommandServiceDependency) {
    this.roleCommandService = roleCommandServiceDependency;
  }

  /**
   * Handles the application-ready event.
   *
   * @param event application ready event
   */
  @EventListener
  public void on(final ApplicationReadyEvent event) {
    var applicationName = event.getApplicationContext().getId();
    logger.info(
        "Starting to verify if roles seeding is needed for {} at {}",
        applicationName,
        getCurrentTimestamp()
    );
    var seedRolesCommand = new SeedRolesCommand();
    roleCommandService.handle(seedRolesCommand);
    logger.info(
        "Roles seeding verification finished for {} at {}",
        applicationName,
        getCurrentTimestamp()
    );
  }

  /**
   * Returns the current timestamp.
   *
   * @return current timestamp
   */
  private Timestamp getCurrentTimestamp() {
    return new Timestamp(System.currentTimeMillis());
  }
}
