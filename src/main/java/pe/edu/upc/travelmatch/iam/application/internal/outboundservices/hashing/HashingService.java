package pe.edu.upc.travelmatch.iam.application.internal.outboundservices.hashing;

/**
 * Service used to encode and validate passwords.
 */
public interface HashingService {
  /**
   * Encodes the given raw password.
   *
   * @param rawPassword raw password
   * @return encoded password
   */
  String encode(CharSequence rawPassword);

  /**
   * Checks whether the raw password matches the encoded password.
   *
   * @param rawPassword raw password
   * @param encodedPassword encoded password
   * @return true if both values match
   */
  boolean matches(CharSequence rawPassword, String encodedPassword);
}
