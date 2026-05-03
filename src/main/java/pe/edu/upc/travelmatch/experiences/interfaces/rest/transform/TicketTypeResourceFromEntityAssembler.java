package pe.edu.upc.travelmatch.experiences.interfaces.rest.transform;

import pe.edu.upc.travelmatch.experiences.domain.model.entities.TicketType;
import pe.edu.upc.travelmatch.experiences.interfaces.rest.resources.TicketTypeResource;

public class TicketTypeResourceFromEntityAssembler {
    public static TicketTypeResource toResourceFromEntity(TicketType entity) {
        return new TicketTypeResource(entity.getId(), entity.getTicketTypeName());
    }
}
