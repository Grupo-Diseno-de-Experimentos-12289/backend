package pe.edu.upc.travelmatch.agencies.interfaces.rest.transform;

import pe.edu.upc.travelmatch.agencies.domain.model.aggregates.AgencyStaff;
import pe.edu.upc.travelmatch.agencies.interfaces.rest.resources.AgencyStaffResource;

public class AgencyStaffResourceFromEntityAssembler {
    public static AgencyStaffResource toResourceFromEntity(AgencyStaff entity) {
        return new AgencyStaffResource(
                entity.getId(),
                entity.getAgency().getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getPosition()
        );
    }
}