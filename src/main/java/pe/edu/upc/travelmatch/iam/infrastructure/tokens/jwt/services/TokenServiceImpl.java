package pe.edu.upc.travelmatch.iam.infrastructure.tokens.jwt.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pe.edu.upc.travelmatch.iam.infrastructure.tokens.jwt.BearerTokenService;

/**
 * JWT-based implementation of {@link BearerTokenService}.
 */
@Service
public final class TokenServiceImpl implements BearerTokenService {
  /**
   * Logger used by the service.
   */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(TokenServiceImpl.class);

  /**
   * Authorization header name.
   */
  private static final String AUTHORIZATION_PARAMETER_NAME = "Authorization";
  /**
   * Bearer token prefix.
   */
  private static final String BEARER_TOKEN_PREFIX = "Bearer ";
  /**
   * Starting index for bearer token extraction.
   */
  private static final int TOKEN_BEGIN_INDEX = 7;

  /**
   * JWT secret.
   */
  @Value("${authorization.jwt.secret}")
  private String secret;

  /**
   * JWT expiration in days.
   */
  @Value("${authorization.jwt.expiration.days}")
  private int expirationDays;

  /**
   * Returns the signing key.
   *
   * @return signing key
   */
  private SecretKey getSigningKey() {
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Builds a token using the default JWT parameters.
   *
   * @param email user email
   * @return generated token
   */
  private String buildTokenWithDefaultParameters(final String email) {
    var issuedAt = new Date();
    var expiration = DateUtils.addDays(issuedAt, expirationDays);
    var key = getSigningKey();
    return Jwts.builder()
        .subject(email)
        .issuedAt(issuedAt)
        .expiration(expiration)
        .signWith(key)
        .compact();
  }

  /**
   * Validates a token.
   *
   * @param token token value
   * @return true if the token is valid
   */
  @Override
  public boolean validateToken(final String token) {
    try {
      Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token);
      LOGGER.info("Token is valid");
      return true;
    } catch (SignatureException e) {
      LOGGER.error("Invalid JSON Web Token signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      LOGGER.error("Invalid JSON Web Token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      LOGGER.error("Expired JSON Web Token: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      LOGGER.error("Unsupported JSON Web Token: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      LOGGER.error(
          "Empty or null claims in JSON Web Token: {}",
          e.getMessage()
      );
    }
    return false;
  }

  /**
   * Generates a token from an authentication object.
   *
   * @param authentication authentication context
   * @return generated token
   */
  @Override
  public String generateToken(final Authentication authentication) {
    return buildTokenWithDefaultParameters(authentication.getName());
  }

  /**
   * Generates a token for an email.
   *
   * @param email user email
   * @return generated token
   */
  @Override
  public String generateToken(final String email) {
    return buildTokenWithDefaultParameters(email);
  }

  /**
   * Checks whether the authorization value has text.
   *
   * @param authorizationParameter authorization value
   * @return true if it has text
   */
  private boolean isTokenPresentIn(final String authorizationParameter) {
    return StringUtils.hasText(authorizationParameter);
  }

  /**
   * Checks whether the authorization value is a bearer token.
   *
   * @param authorizationParameter authorization value
   * @return true if it starts with the bearer prefix
   */
  private boolean isBearerTokenIn(final String authorizationParameter) {
    return authorizationParameter.startsWith(BEARER_TOKEN_PREFIX);
  }

  /**
   * Extracts the token portion from a bearer header.
   *
   * @param authorizationHeaderParameter authorization header value
   * @return bearer token
   */
  private String extractTokenFrom(final String authorizationHeaderParameter) {
    return authorizationHeaderParameter.substring(TOKEN_BEGIN_INDEX);
  }

  /**
   * Returns the authorization header value.
   *
   * @param request HTTP request
   * @return authorization header value
   */
  private String getAuthorizationParameterFrom(
      final HttpServletRequest request
  ) {
    return request.getHeader(AUTHORIZATION_PARAMETER_NAME);
  }

  /**
   * Extracts the bearer token from an HTTP request.
   *
   * @param request HTTP request
   * @return bearer token or null when absent/invalid
   */
  @Override
  public String getBearerTokenFrom(final HttpServletRequest request) {
    String parameter = getAuthorizationParameterFrom(request);
    if (isTokenPresentIn(parameter)) {
      if (isBearerTokenIn(parameter)) {
        return extractTokenFrom(parameter);
      } else {
        LOGGER.warn("Authorization header is not a Bearer token");
      }
    } else {
      LOGGER.warn("Authorization header is not present");
    }
    return null;
  }

  /**
   * Extracts all claims from a token.
   *
   * @param token token value
   * @return token claims
   */
  private Claims extractAllClaims(final String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /**
   * Extracts a claim from a token.
   *
   * @param token token value
   * @param claimsResolver claim resolver
   * @param <T> claim type
   * @return resolved claim
   */
  private <T> T extractClaim(
      final String token,
      final Function<Claims, T> claimsResolver
  ) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Extracts the email from a token.
   *
   * @param token token value
   * @return email subject
   */
  @Override
  public String getEmailFromToken(final String token) {
    return extractClaim(token, Claims::getSubject);
  }
}
