package pe.edu.upc.travelmatch.iam.infrastructure.tokens.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import pe.edu.upc.travelmatch.iam.application.internal.outboundservices.tokens.TokenService;

/**
 * Token service specialized in handling bearer tokens from HTTP requests.
 */
public interface BearerTokenService extends TokenService {
  /**
   * Extracts the bearer token from the given request.
   *
   * @param request the HTTP request
   * @return the bearer token, or null when not present
   */
  String getBearerTokenFrom(HttpServletRequest request);

  /**
   * Generates a token from the authenticated principal.
   *
   * @param authentication the authentication context
   * @return the generated token
   */
  String generateToken(Authentication authentication);
}
