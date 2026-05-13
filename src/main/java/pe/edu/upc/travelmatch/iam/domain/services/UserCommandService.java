package pe.edu.upc.travelmatch.iam.domain.services;

import java.util.Optional;
import org.apache.commons.lang3.tuple.ImmutablePair;
import pe.edu.upc.travelmatch.iam.domain.model.aggregates.User;
import pe.edu.upc.travelmatch.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.travelmatch.iam.domain.model.commands.SignUpCommand;

/**
 * User command service interface.
 */
public interface UserCommandService {

  /**
   * Handle sign up command.
   *
   * @param command the sign up command
   * @return the user if successful
   */
  Optional<User> handle(SignUpCommand command);

  /**
   * Handle sign in command.
   *
   * @param command the sign in command
   * @return the user and token if successful
   */
  Optional<ImmutablePair<User, String>> handle(SignInCommand command);
}
