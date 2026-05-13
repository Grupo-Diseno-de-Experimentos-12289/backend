package pe.edu.upc.travelmatch.iam.domain.services;

import java.util.List;
import java.util.Optional;
import pe.edu.upc.travelmatch.iam.domain.model.aggregates.User;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetAllUsersQuery;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetUserByEmailQuery;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetUserByIdQuery;

/** UserQueryService contract. */
public interface UserQueryService {
  /** Handle. */
  List<User> handle(GetAllUsersQuery query);

  /** Handle. */
  Optional<User> handle(GetUserByIdQuery query);

  /** Handle. */
  Optional<User> handle(GetUserByEmailQuery query);
}
