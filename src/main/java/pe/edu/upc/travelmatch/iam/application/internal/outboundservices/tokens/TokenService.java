package pe.edu.upc.travelmatch.iam.application.internal.outboundservices.tokens;

/**
 * Service responsible for generating and validating authentication tokens.
 */
public interface TokenService {
  /**
   * Generates a token for the given email.
   *
   * @param email user email
   * @return generated token
   */
  String generateToken(String email);

  /**
   * Extracts an email from the given token.
   *
   * @param token token value
   * @return email contained in the token
   */
  String getEmailFromToken(String token);

  /**
   * Validates a token.
   *
   * @param token token value
   * @return true if the token is valid
   */
  boolean validateToken(String token);
}
