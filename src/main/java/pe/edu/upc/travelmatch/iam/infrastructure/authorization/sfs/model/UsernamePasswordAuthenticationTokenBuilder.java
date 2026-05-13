package pe.edu.upc.travelmatch.iam.infrastructure.authorization.sfs.model;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

/**
 * Builds Spring Security username/password authentication tokens.
 */
public final class UsernamePasswordAuthenticationTokenBuilder {
  private UsernamePasswordAuthenticationTokenBuilder() {
  }

  /**
   * Builds an authentication token.
   *
   * @param principal the authenticated principal
   * @param request the current HTTP request
   * @return authentication token
   */
  public static UsernamePasswordAuthenticationToken build(
      final UserDetails principal,
      final HttpServletRequest request
  ) {
    var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
        principal,
        null,
        principal.getAuthorities()
    );
    usernamePasswordAuthenticationToken.setDetails(
        new WebAuthenticationDetailsSource().buildDetails(request)
    );
    return usernamePasswordAuthenticationToken;
  }
}