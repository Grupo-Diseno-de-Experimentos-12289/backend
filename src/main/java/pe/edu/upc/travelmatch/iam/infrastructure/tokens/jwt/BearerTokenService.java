package pe.edu.upc.travelmatch.iam.infrastructure.tokens.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import pe.edu.upc.travelmatch.iam.application.internal.outboundservices.tokens.TokenService;

/** BearerTokenService contract. */
public interface BearerTokenService extends TokenService {
  /** Get bearer token from. */
  String getBearerTokenFrom(HttpServletRequest request);

  /** Generate token. */
  String generateToken(Authentication authentication);
}
