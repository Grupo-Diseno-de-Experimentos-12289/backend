package pe.edu.upc.travelmatch.iam.infrastructure.authorization.sfs.pipeline;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Unauthorized Request Handler Entry Point.
 */
@Component
public class UnauthorizedRequestHandlerEntryPoint implements AuthenticationEntryPoint {
  /**
   * Logger used by the entry point.
   */
  private static final Logger LOGGER =
      LoggerFactory.getLogger(UnauthorizedRequestHandlerEntryPoint.class);

  @Override
  public void commence(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final AuthenticationException authorizationException
  ) throws IOException, ServletException {
    LOGGER.error(
        "Unauthorized request: {}",
        authorizationException.getMessage()
    );
    response.sendError(
        HttpServletResponse.SC_UNAUTHORIZED,
        "Unauthorized request detected"
    );
  }
}