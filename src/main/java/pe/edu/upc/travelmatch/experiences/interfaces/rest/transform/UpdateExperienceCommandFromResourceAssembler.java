package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import pe.edu.upc.travelmatch.experiences.domain.model.commands.UpdateExperienceCommand;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.CancellationPolicyType;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.DestinationId;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.UpdateExperienceResource;

/** UpdateExperienceCommandFromResourceAssembler. */
public class UpdateExperienceCommandFromResourceAssembler {
  /** To command from resource. */
  public static UpdateExperienceCommand toCommandFromResource(
      UpdateExperienceResource resource, Long id) {
    CancellationPolicyType cancellationPolicyType =
        resource.cancellationPolicyType() == null || resource.cancellationPolicyType().isBlank()
            ? null
            : CancellationPolicyType.valueOf(resource.cancellationPolicyType().toUpperCase());
    return new UpdateExperienceCommand(
        id,
        resource.title(),
        resource.description(),
        resource.category(),
        new DestinationId(resource.destinationId()),
        resource.duration(),
        resource.meetingPoint(),
        cancellationPolicyType,
        resource.cancellationPolicyDescription());
  }
}
