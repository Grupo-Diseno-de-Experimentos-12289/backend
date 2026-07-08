package pe.edu.upc.travelmatch.iam.domain.model.commands;

/**
 * Command to update a user's profile information including personal details, profile type and
 * avatar.
 */
public record UpdateUserProfileCommand(
    Long userId,
    String firstName,
    String lastName,
    String phone,
    String profileType,
    String avatarUrl) {}
