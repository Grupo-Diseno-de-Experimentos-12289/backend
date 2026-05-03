package pe.edu.upc.travelmatch.profiles.domain.model.commands;

import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.AvailabilityId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Quantity;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;

public record AddCartItemCommand(UserId userId, AvailabilityId availabilityId, Quantity quantity) {
}
