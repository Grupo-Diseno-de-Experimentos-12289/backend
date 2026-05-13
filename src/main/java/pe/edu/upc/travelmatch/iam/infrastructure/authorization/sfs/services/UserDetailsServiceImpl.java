package pe.edu.upc.travelmatch.iam.infrastructure.authorization.sfs.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import pe.edu.upc.travelmatch.iam.infrastructure.persistence.jpa.repositories.UserRepository;

/**
 * Loads Spring Security user details from the IAM user repository.
 */
@Service(value = "defaultUserDetailsService")
public final class UserDetailsServiceImpl implements UserDetailsService {
  /**
   * User repository dependency.
   */
  private final UserRepository userRepository;

  /**
   * Constructor.
   *
   * @param userRepositoryDependency user repository dependency
   */
  public UserDetailsServiceImpl(final UserRepository userRepositoryDependency) {
    this.userRepository = userRepositoryDependency;
  }

  /**
   * Loads a user by username (email).
   *
   * @param email user email
   * @return user details
   * @throws UsernameNotFoundException when the user is not found
   */
  @Override
  public UserDetails loadUserByUsername(
      final String email
  ) throws UsernameNotFoundException {
    var user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(
            "User Not Found with email: "
                + email
        ));
    return UserDetailsImpl.build(user);
  }
}
