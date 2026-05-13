package pe.edu.upc.travelmatch.iam.infrastructure.hashing.bcrypt.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.iam.infrastructure.hashing.bcrypt.BcryptHashingService;

/**
 * Bcrypt implementation of the IAM hashing service.
 */
@Service
public final class HashingServiceImpl implements BcryptHashingService {
  /** Password encoder. */
  private final BCryptPasswordEncoder passwordEncoder;

  /** Default constructor. */
  public HashingServiceImpl() {
    this.passwordEncoder = new BCryptPasswordEncoder();
  }

  /**
   * Encodes a password.
   *
   * @param rawPassword raw password
   * @return encoded password
   */
  @Override
  public String encode(final CharSequence rawPassword) {
    return passwordEncoder.encode(rawPassword);
  }

  /**
   * Checks whether a password matches the encoded value.
   *
   * @param rawPassword raw password
   * @param encodedPassword encoded password
   * @return true if the passwords match
   */
  @Override
  public boolean matches(
      final CharSequence rawPassword,
      final String encodedPassword
  ) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }
}
