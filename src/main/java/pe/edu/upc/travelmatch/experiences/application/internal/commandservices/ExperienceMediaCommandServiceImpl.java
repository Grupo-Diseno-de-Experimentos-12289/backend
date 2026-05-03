package pe.edu.upc.travelmatch.experiences.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateExperienceMediaCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateExperienceMediaCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.ExperienceMedia;
import pe.edu.upc.travelmatch.experiences.domain.services.ExperienceMediaCommandService;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.ExperienceMediaRepository;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.ExperienceRepository;

import java.util.Optional;

@Service
public class ExperienceMediaCommandServiceImpl implements ExperienceMediaCommandService {
    private final ExperienceMediaRepository mediaRepository;
    private final ExperienceRepository experienceRepository;

    public ExperienceMediaCommandServiceImpl(ExperienceMediaRepository mediaRepository,
                                             ExperienceRepository experienceRepository) {
        this.mediaRepository = mediaRepository;
        this.experienceRepository = experienceRepository;
    }

    @Override
    public Long handle(CreateExperienceMediaCommand command) {
        var experience = experienceRepository.findById(command.experience().getId())
                .orElseThrow(() -> new IllegalArgumentException("Experience with ID " + command.experience().getId() + " does not exist."));

        var media = new ExperienceMedia(
                experience,
                command.mediaUrl(),
                command.caption()
        );

        var saved = mediaRepository.save(media);
        return saved.getId();
    }

    @Override
    public Optional<ExperienceMedia> handle(UpdateExperienceMediaCommand command) {
        return mediaRepository.findById(command.id()).map(media -> {
            media.update(command.mediaUrl(), command.caption());
            return mediaRepository.save(media);
        });
    }

    @Override
    public void deleteById(Long id) {
        mediaRepository.findById(id).ifPresent(media -> {
            media.markAsDeleted();
            mediaRepository.save(media);
        });
    }
}