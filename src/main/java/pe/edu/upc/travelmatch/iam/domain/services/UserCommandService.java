package pe.edu.upc.travelmatch.iam.domain.services;

import java.util.Optional;
import org.apache.commons.lang3.tuple.ImmutablePair;
import pe.edu.upc.travelmatch.iam.domain.model.aggregates.User;
import pe.edu.upc.travelmatch.iam.domain.model.commands.SignInCommand;
import pe.edu.upc.travelmatch.iam.domain.model.commands.SignUpCommand;

/** UserCommandService contract. */
public interface UserCommandService {
  /** Handle. */
  Optional<User> handle(SignUpCommand command);

  /** Handle. */
  Optional<ImmutablePair<User, String>> handle(SignInCommand command);
}
