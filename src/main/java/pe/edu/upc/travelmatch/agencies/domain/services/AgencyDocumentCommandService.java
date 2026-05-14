package pe.edu.upc.travelmatch.agencies.domain.services;

import java.util.Optional;
import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyDocument;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.CreateAgencyDocumentCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.DeleteAgencyDocumentCommand;
import pe.edu.upc.travelmatch.agencies.domain.model.commands.UpdateAgencyDocumentCommand;

/** AgencyDocumentCommandService contract. */
public interface AgencyDocumentCommandService {

  /** Handle. */
  Optional<AgencyDocument> handle(CreateAgencyDocumentCommand command);

  /** Handle. */
  Optional<AgencyDocument> handle(UpdateAgencyDocumentCommand command);

  /** Handle. */
  void handle(DeleteAgencyDocumentCommand command);
}
