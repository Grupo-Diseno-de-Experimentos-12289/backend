package pe.edu.upc.travelmatch.iam.infrastructure.authorization.sfs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pe.edu.upc.travelmatch.iam.domain.model.aggregates.User;

/**
 * Implementation of UserDetails.
 */
@Getter
@EqualsAndHashCode
public final class UserDetailsImpl implements UserDetails {
  private final String email;

  @JsonIgnore
  private final String password;

  private final boolean accountNonExpired;
  private final boolean accountNonLocked;
  private final boolean credentialsNonExpired;
  private final boolean enabled;
  private final Collection<? extends GrantedAuthority> authorities;

  /**
   * Constructor.
   *
   * @param email                user email
   * @param password             user password
   * @param authoritiesDependency granted authorities
   */
  public UserDetailsImpl(
      final String email,
      final String password,
      final Collection<? extends GrantedAuthority> authoritiesDependency
  ) {
    this.email = email;
    this.password = password;
    this.authorities = authoritiesDependency;
    this.accountNonExpired = true;
    this.accountNonLocked = true;
    this.credentialsNonExpired = true;
    this.enabled = true;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  /**
   * Builds a {@link UserDetailsImpl} from a user entity.
   *
   * @param user user entity
   * @return user details implementation
   */
  public static UserDetailsImpl build(final User user) {
    var authorities = user.getRoles().stream()
        .map(role -> role.getName().name())
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
    return new UserDetailsImpl(user.getEmail(), user.getPassword(), authorities);
  }
}
