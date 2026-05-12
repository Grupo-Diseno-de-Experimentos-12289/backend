package pe.edu.upc.travelmatch.experiences.application.internal.eventhandlers;

import java.sql.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.SeedCategoriesCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.SeedTicketTypesCommand;
import pe.edu.upc.travelmatch.experiences.domain.services.CategoryCommandService;
import pe.edu.upc.travelmatch.experiences.domain.services.TicketTypeCommandService;

/**
 * Event handler for executing experience seeders upon application ready.
 */
@Service
public class ExperienceSeedersEventHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExperienceSeedersEventHandler.class);
  private final CategoryCommandService categoryCommandService;
  private final TicketTypeCommandService ticketTypeCommandService;

  /**
   * Constructs an ExperienceSeedersEventHandler.
   *
   * @param categoryCommandService   the category command service
   * @param ticketTypeCommandService the ticket type command service
   */
  public ExperienceSeedersEventHandler(
      final CategoryCommandService categoryCommandService,
      final TicketTypeCommandService ticketTypeCommandService) {
    this.categoryCommandService = categoryCommandService;
    this.ticketTypeCommandService = ticketTypeCommandService;
  }

  /**
   * Triggers the seeding process when the application is ready.
   *
   * @param event the application ready event
   */
  @EventListener
  public void on(final ApplicationReadyEvent event) {
    var applicationName = event.getApplicationContext().getId();
    LOGGER.info("Starting to verify seeding of categories and ticket types for {} at {}",
        applicationName, getCurrentTimestamp());

    try {
      LOGGER.info("Seeding categories...");
      var seedCategoriesCommand = new SeedCategoriesCommand();
      categoryCommandService.handle(seedCategoriesCommand);
      LOGGER.info("Categories seeding completed.");

      LOGGER.info("Seeding ticket types...");
      var seedTicketTypesCommand = new SeedTicketTypesCommand();
      ticketTypeCommandService.handle(seedTicketTypesCommand);
      LOGGER.info("Ticket types seeding completed.");
    } catch (Exception e) {
      LOGGER.error("Error while seeding categories or ticket types: {}", e.getMessage());
    }

    LOGGER.info("Seeding verification finished for {} at {}",
        applicationName, getCurrentTimestamp());
  }

  private Timestamp getCurrentTimestamp() {
    return new Timestamp(System.currentTimeMillis());
  }
}