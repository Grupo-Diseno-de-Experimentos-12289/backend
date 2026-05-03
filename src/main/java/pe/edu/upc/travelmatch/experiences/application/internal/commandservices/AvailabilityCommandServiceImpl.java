package pe.edu.upc.travelmatch.experiences.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Availability;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateAvailabilityCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateAvailabilityCommand;
import pe.edu.upc.travelmatch.experiences.domain.services.AvailabilityCommandService;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.AvailabilityRepository;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.ExperienceRepository;

@Service
public class AvailabilityCommandServiceImpl implements AvailabilityCommandService {

    private final AvailabilityRepository availabilityRepository;
    private final ExperienceRepository experienceRepository;

    public AvailabilityCommandServiceImpl(
            AvailabilityRepository availabilityRepository,
            ExperienceRepository experienceRepository
    ) {
        this.availabilityRepository = availabilityRepository;
        this.experienceRepository = experienceRepository;
    }

    @Override
    public Long handle(CreateAvailabilityCommand command) {
        var experience = experienceRepository.findById(command.experience().getId())
                .orElseThrow(() -> new IllegalArgumentException("Experience with ID " + command.experience().getId() + " does not exist."));

        var availability = new Availability(
                experience,
                command.startDateTime(),
                command.endDateTime(),
                command.capacity()
        );

        var saved = availabilityRepository.save(availability);
        return saved.getId();
    }

    @Override
    public void updateAvailability(UpdateAvailabilityCommand command) {
        var availability = availabilityRepository.findById(command.availabilityId())
                .orElseThrow(() -> new RuntimeException("Availability not found"));

        availability.updateInfo(
                command.startDateTime(),
                command.endDateTime(),
                command.capacity()
        );

        availabilityRepository.save(availability);
    }

    @Override
    public void deleteAvailability(Long id) {
        var availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability not found"));

        availability.markAsDeleted();
        availabilityRepository.save(availability);
    }
}