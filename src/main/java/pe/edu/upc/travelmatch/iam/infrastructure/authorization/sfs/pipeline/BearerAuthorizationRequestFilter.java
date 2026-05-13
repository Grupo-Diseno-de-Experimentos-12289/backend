package pe.edu.upc.travelmatch.iam.infrastructure.authorization.sfs.pipeline;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import pe.edu.upc.travelmatch.iam.infrastructure.authorization.sfs.model.UsernamePasswordAuthenticationTokenBuilder;
import pe.edu.upc.travelmatch.iam.infrastructure.tokens.jwt.BearerTokenService;

/**
 * Filter that resolves bearer tokens and populates the security context.
 */
public class BearerAuthorizationRequestFilter extends OncePerRequestFilter {
  /**
   * Logger used by the filter.
   */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(BearerAuthorizationRequestFilter.class);
  /**
   * Token service dependency.
   */
  private final BearerTokenService tokenService;
  /**
   * User details service dependency.
   */
  @Qualifier("defaultUserDetailsService")
  private final UserDetailsService userDetailsService;

  /**
   * Constructor.
   *
   * @param tokenServiceDependency       token service dependency
   * @param userDetailsServiceDependency user details service dependency
   */
  public BearerAuthorizationRequestFilter(
      final BearerTokenService tokenServiceDependency,
      final UserDetailsService userDetailsServiceDependency
  ) {
    this.tokenService = tokenServiceDependency;
    this.userDetailsService = userDetailsServiceDependency;
  }

  /**
   * Filters the request and populates authentication when a valid bearer token is present.
   */
  @Override
  protected void doFilterInternal(
      @NonNull final HttpServletRequest request,
      @NonNull final HttpServletResponse response,
      @NonNull final FilterChain filterChain
  ) throws ServletException, IOException {
    try {
      String token = tokenService.getBearerTokenFrom(request);
      LOGGER.info("Token: {}", token);
      if (token != null && tokenService.validateToken(token)) {
        String email = tokenService.getEmailFromToken(token);
        LOGGER.info("Email extracted from token: {}", email);
        var userDetails = userDetailsService.loadUserByUsername(email);
        SecurityContextHolder.getContext()
            .setAuthentication(
                UsernamePasswordAuthenticationTokenBuilder.build(
                    userDetails,
                    request
                )
            );
      } else {
        LOGGER.info("Token is not valid");
      }
    } catch (Exception e) {
      LOGGER.error("Cannot set user authentication: {}", e.getMessage());
    }
    filterChain.doFilter(request, response);
  }
}
