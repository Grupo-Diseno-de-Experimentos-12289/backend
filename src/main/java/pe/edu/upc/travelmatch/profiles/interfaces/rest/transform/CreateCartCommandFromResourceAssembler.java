package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import pe.edu.upc.travelmatch.profiles.domain.model.commands.CreateCartCommand;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.CreateCartResource;

public class CreateCartCommandFromResourceAssembler {
    public static CreateCartCommand toCommandFromResource(CreateCartResource resource) {
        return new CreateCartCommand(new UserId(resource.userId()));
    }
}
