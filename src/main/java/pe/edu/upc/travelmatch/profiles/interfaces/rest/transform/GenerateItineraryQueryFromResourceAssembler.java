package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import pe.edu.upc.travelmatch.profiles.domain.model.queries.GenerateItineraryQuery;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.GenerateItineraryResource;

/** GenerateItineraryQueryFromResourceAssembler type. */
public class GenerateItineraryQueryFromResourceAssembler {
    /** To query from resource. */
    public static GenerateItineraryQuery toQueryFromResource(GenerateItineraryResource resource) {
        return new GenerateItineraryQuery(
                resource.destinationId(), resource.categories(), resource.numberOfDays());
    }
}