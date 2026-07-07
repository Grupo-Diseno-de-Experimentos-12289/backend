package pe.edu.upc.travelmatch.iam.application.internal.outboundservices.tokens;

/** TokenService contract. */
public interface TokenService {
  /** Generate token. */
  String generateToken(String email);

  /** Get email from token. */
  String getEmailFromToken(String token);

  /** Validate token. */
  boolean validateToken(String token);
}
