package pe.edu.upc.travelmatch.experiences.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.experiences.application.internal.outboundservices.acl.ExternalIamService;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateExperienceCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateExperienceCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.AgencyId;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.Categories;
import pe.edu.upc.travelmatch.experiences.domain.services.ExperienceCommandService;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.CategoryRepository;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.ExperienceRepository;

@Service
public class ExperienceCommandServiceImpl implements ExperienceCommandService {

    private final ExperienceRepository experienceRepository;
    private final CategoryRepository categoryRepository;
    private final ExternalIamService externalIamService;

    public ExperienceCommandServiceImpl(
            ExperienceRepository experienceRepository,
            CategoryRepository categoryRepository,
            ExternalIamService externalIamService
    ) {
        this.experienceRepository = experienceRepository;
        this.categoryRepository = categoryRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    public Long handle(CreateExperienceCommand command) {
        externalIamService.validateAgencyExists(command.agencyId());
        Categories categoryEnum = Categories.valueOf(command.category());
        var category = categoryRepository.findByName(categoryEnum)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Category not found with name: " + command.category()));
        var experience = new Experience(
                command.title(),
                command.description(),
                new AgencyId(command.agencyId()),
                category,
                command.destinationId(),
                command.duration(),
                command.meetingPoint()
        );
        var createdExperience = experienceRepository.save(experience);
        return createdExperience.getId();
    }

    @Override
    public void updateExperience(UpdateExperienceCommand command) {
        var experience = experienceRepository.findById(command.id())
                .orElseThrow(() -> new RuntimeException("Experience not found"));
        Categories categoryEnum = Categories.valueOf(command.category());
        var category = categoryRepository.findByName(categoryEnum)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + command.category()));

        experience.updateInfo(
                command.title(),
                command.description(),
                category,
                command.destinationId(),
                command.duration(),
                command.meetingPoint()
        );
        experienceRepository.save(experience);
    }

    @Override
    public void deleteExperience(Long experienceId) {
        var experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new RuntimeException("Experience not found"));
        experience.markAsDeleted();
        experienceRepository.save(experience);
    }
}