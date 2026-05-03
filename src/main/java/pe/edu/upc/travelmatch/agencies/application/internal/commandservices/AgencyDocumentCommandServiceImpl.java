package pe.edu.upc.travelmatch.agencies.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.Agency;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyDocument;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyDocumentCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyDocumentCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyDocumentCommand;
import pe.edu.upc.travelmatch.agencies.domain.services.AgencyDocumentCommandService;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyRepository;
import pe.edu.upc.travelmatch.agencies.infrastructure.persistence.jpa.repositories.AgencyDocumentRepository;

import java.util.Optional;

@Service
public class AgencyDocumentCommandServiceImpl implements AgencyDocumentCommandService {

    private final AgencyDocumentRepository agencyDocumentRepository;
    private final AgencyRepository agencyRepository;

    public AgencyDocumentCommandServiceImpl(AgencyDocumentRepository agencyDocumentRepository, AgencyRepository agencyRepository) {
        this.agencyDocumentRepository = agencyDocumentRepository;
        this.agencyRepository = agencyRepository;
    }

    @Override
    public Optional<AgencyDocument> handle(CreateAgencyDocumentCommand command) {

        Optional<Agency> agency = agencyRepository.findById(command.agencyId());
        if (agency.isEmpty()) {
            throw new IllegalArgumentException("Agency with ID " + command.agencyId() + " does not exist.");
        }

        var agencyDocument = new AgencyDocument(
                agency.get(),
                command.documentType(),
                command.documentUrl(),
                command.description()
        );
        return Optional.of(agencyDocumentRepository.save(agencyDocument));
    }

    @Override
    public Optional<AgencyDocument> handle(UpdateAgencyDocumentCommand command) {

        Optional<AgencyDocument> existingDocument = agencyDocumentRepository.findById(command.id());
        if (existingDocument.isEmpty()) {
            return Optional.empty();
        }

        AgencyDocument documentToUpdate = existingDocument.get();
        documentToUpdate.update(
                command.documentType(),
                command.documentUrl(),
                command.description()
        );
        return Optional.of(agencyDocumentRepository.save(documentToUpdate));
    }

    @Override
    public void handle(DeleteAgencyDocumentCommand command) {

        if (!agencyDocumentRepository.existsById(command.id())) {
            throw new IllegalArgumentException("Document with ID " + command.id() + " does not exist.");
        }
        agencyDocumentRepository.deleteById(command.id());
    }
}