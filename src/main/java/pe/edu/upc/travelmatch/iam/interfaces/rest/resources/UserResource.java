package pe.edu.upc.travelmatch.iam.interfaces.rest.resources;

import java.util.List;

public record UserResource(Long id, String email, String firstName, String lastName, String phone, List<String> roles) {
}
