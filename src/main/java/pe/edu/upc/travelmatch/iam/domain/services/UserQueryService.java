package pe.edu.upc.travelmatch.iam.domain.services;

import java.util.List;
import java.util.Optional;
import pe.edu.upc.travelmatch.iam.domain.model.aggregates.User;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetAllUsersQuery;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetUserByEmailQuery;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetUserByIdQuery;

/**
 * User query service interface.
 */
public interface UserQueryService {

  /**
   * Handle get all users query.
   *
   * @param query the get all users query
   * @return the list of users
   */
  List<User> handle(GetAllUsersQuery query);

  /**
   * Handle get user by id query.
   *
   * @param query the get user by id query
   * @return the user if found
   */
  Optional<User> handle(GetUserByIdQuery query);

  /**
   * Handle get user by email query.
   *
   * @param query the get user by email query
   * @return the user if found
   */
  Optional<User> handle(GetUserByEmailQuery query);
}