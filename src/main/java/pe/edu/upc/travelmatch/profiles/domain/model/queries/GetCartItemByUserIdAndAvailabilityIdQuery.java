package pe.edu.upc.travelmatch.profiles.domain.model.queries;

import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;

public record GetCartItemByUserIdAndAvailabilityIdQuery(UserId userId, AvailabilityId availabilityId) {
}
