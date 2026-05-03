package pe.edu.upc.travelmatch.experiences.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.ExperienceMedia;
import pe.edu.upc.travelmatch.experiences.domain.services.ExperienceMediaQueryService;
import pe.edu.upc.travelmatch.experiences.infrastructure.persistence.jpa.repositories.ExperienceMediaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ExperienceMediaQueryServiceImpl implements ExperienceMediaQueryService {
    private final ExperienceMediaRepository repository;

    public ExperienceMediaQueryServiceImpl(ExperienceMediaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ExperienceMedia> getAll() {
        return repository.findAll();
    }

    @Override
    public List<ExperienceMedia> findByExperienceId(Long experienceId) {
        return repository.findByExperienceId(experienceId);
    }

    @Override
    public Optional<ExperienceMedia> findById(Long id) {
        return repository.findById(id);
    }
}