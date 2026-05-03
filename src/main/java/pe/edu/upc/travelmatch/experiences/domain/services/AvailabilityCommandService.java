package pe.edu.upc.travelmatch.experiences.domain.services;

import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateAvailabilityCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateAvailabilityCommand;

public interface AvailabilityCommandService {
    Long handle(CreateAvailabilityCommand command);
    void updateAvailability(UpdateAvailabilityCommand command);
    void deleteAvailability(Long availabilityId);
}