package pe.edu.upc.travelmatch.iam.domain.services;

import java.util.Optional;
import org.apache.commons.lang3.tuple.ImmutablePair;
import pe.edu.upc.travelmatch.iam.domain.model.aggregates.User;
import pe.edu.upc.travelmatch.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.travelmatch.iam.domain.model.commands.SignUpCommand;
import pe.edu.upc.travelmatch.iam.domain.model.commands.UpdateUserProfileCommand;

/** UserCommandService contract. */
public interface UserCommandService {
  /** Handle sign-up. */
  Optional<User> handle(SignUpCommand command);

  /** Handle sign-in. */
  Optional<ImmutablePair<User, String>> handle(SignInCommand command);

  /** Handle profile update. */
  Optional<User> handle(UpdateUserProfileCommand command);
}

