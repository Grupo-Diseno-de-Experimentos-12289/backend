package pe.edu.upc.travelmatch.profiles.domain.model.commands;

import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Rating;

/** UpdateReviewCommand value carrier. */
public record UpdateReviewCommand(Long reviewId, Rating rating, String comment) {}
