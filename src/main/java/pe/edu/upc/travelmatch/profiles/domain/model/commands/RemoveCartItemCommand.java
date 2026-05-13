package pe.edu.upc.travelmatch.profiles.domain.model.commands;

import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;

/** RemoveCartItemCommand value carrier. */
public record RemoveCartItemCommand(UserId userId, AvailabilityId availabilityId) {}
