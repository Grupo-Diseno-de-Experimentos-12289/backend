package pe.edu.upc.travelmatch.experiences.application.internal.eventhandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.SeedCategoriesCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.SeedTicketTypesCommand;
import pe.edu.upc.travelmatch.experiences.domain.services.CategoryCommandService;
import pe.edu.upc.travelmatch.experiences.domain.services.TicketTypeCommandService;

import java.sql.Timestamp;

@Service
public class ExperienceSeedersEventHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(ExperienceSeedersEventHandler.class);
    private final CategoryCommandService categoryCommandService;
    private final TicketTypeCommandService ticketTypeCommandService;

    public ExperienceSeedersEventHandler(
            CategoryCommandService categoryCommandService,
            TicketTypeCommandService ticketTypeCommandService
    ) {
        this.categoryCommandService = categoryCommandService;
        this.ticketTypeCommandService = ticketTypeCommandService;
    }

    @EventListener
    public void on(ApplicationReadyEvent event) {
        var applicationName = event.getApplicationContext().getId();
        LOGGER.info("Starting to verify seeding of categories and ticket types for {} at {}", applicationName, getCurrentTimestamp());

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

        LOGGER.info("Seeding verification finished for {} at {}", applicationName, getCurrentTimestamp());
    }

    private Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}