package pe.edu.upc.travelmatch.agencies.domain.services;

import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyDocument;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyDocumentCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyDocumentCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyDocumentCommand;

import java.util.Optional;

public interface AgencyDocumentCommandService {

    Optional<AgencyDocument> handle(CreateAgencyDocumentCommand command);

    Optional<AgencyDocument> handle(UpdateAgencyDocumentCommand command);

    void handle(DeleteAgencyDocumentCommand command);
}