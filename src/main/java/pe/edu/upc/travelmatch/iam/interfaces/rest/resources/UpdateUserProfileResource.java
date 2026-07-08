package pe.edu.upc.travelmatch.iam.interfaces.rest.resources;

/** Request body for updating a user's profile. */
public record UpdateUserProfileResource(
    String firstName,
    String lastName,
    String phone,
    String profileType,
    String avatarUrl) {}
