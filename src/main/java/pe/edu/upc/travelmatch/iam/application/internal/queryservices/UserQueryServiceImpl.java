package pe.edu.upc.travelmatch.iam.application.internal.queryservices;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.iam.domain.model.aggregates.User;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetAllUsersQuery;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetUserByEmailQuery;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetUserByIdQuery;
import pe.edu.upc.travelmatch.iam.domain.services.UserQueryService;
import pe.edu.upc.travelmatch.iam.infrastructure.persistence.jpa.repositories.UserRepository;

/**
 * Query service that provides read operations related to users.
 */
@Service
public final class UserQueryServiceImpl implements UserQueryService {
  private final UserRepository userRepository;

  /**
   * Constructor.
   *
   * @param userRepositoryDependency repository for users
   */
  public UserQueryServiceImpl(final UserRepository userRepositoryDependency) {
    this.userRepository = userRepositoryDependency;
  }

  /**
   * Returns all users.
   *
   * @param query query object
   * @return list of users
   */
  @Override
  public List<User> handle(final GetAllUsersQuery query) {
    return userRepository.findAll();
  }

  /**
   * Returns a user by id.
   *
   * @param query query object
   * @return optional user
   */
  @Override
  public Optional<User> handle(final GetUserByIdQuery query) {
    return userRepository.findById(query.userId());
  }

  /**
   * Returns a user by email.
   *
   * @param query query object
   * @return optional user
   */
  @Override
  public Optional<User> handle(final GetUserByEmailQuery query) {
    return userRepository.findByEmail(query.email());
  }
}
