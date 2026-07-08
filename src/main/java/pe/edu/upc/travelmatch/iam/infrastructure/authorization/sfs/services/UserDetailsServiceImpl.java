package pe.edu.upc.travelmatch.iam.infrastructure.authorization.sfs.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import pe.edu.upc.travelmatch.iam.infrastructure.persistence.jpa.repositories.UserRepository;

/** UserDetailsServiceImpl type. */
@Service(value = "defaultUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;

  /** Constructs a new UserDetailsServiceImpl. */
  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    var user =
        userRepository
            .findByEmail(email)
            .orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with email: " + email));
    return UserDetailsImpl.build(user);
  }
}
