package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import pe.edu.upc.travelmatch.experiences.domain.model.commands.CreateExperienceCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.CancellationPolicyType;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.DestinationId;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.CreateExperienceResource;

/** CreateExperienceCommandFromResourceAssembler. */
public class CreateExperienceCommandFromResourceAssembler {
  /** To command from resource. */
  public static CreateExperienceCommand toCommandFromResource(
      CreateExperienceResource resource, Long agencyId) {
    CancellationPolicyType cancellationPolicyType =
        resource.cancellationPolicyType() == null || resource.cancellationPolicyType().isBlank()
            ? CancellationPolicyType.FLEXIBLE
            : CancellationPolicyType.valueOf(resource.cancellationPolicyType().toUpperCase());
    return new CreateExperienceCommand(
        resource.title(),
        resource.description(),
        agencyId,
        resource.category(),
        new DestinationId(resource.destinationId()),
        resource.duration(),
        resource.meetingPoint(),
        cancellationPolicyType,
        resource.cancellationPolicyDescription());
  }
}
