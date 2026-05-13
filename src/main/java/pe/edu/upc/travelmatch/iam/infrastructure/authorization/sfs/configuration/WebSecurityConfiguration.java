package pe.edu.upc.travelmatch.iam.infrastructure.authorization.sfs.configuration;

import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import pe.edu.upc.travelmatch.iam.infrastructure.authorization.sfs.pipeline.BearerAuthorizationRequestFilter;
import pe.edu.upc.travelmatch.iam.infrastructure.hashing.bcrypt.BcryptHashingService;
import pe.edu.upc.travelmatch.iam.infrastructure.tokens.jwt.BearerTokenService;

/**
 * Web Security Configuration.
 */
@Configuration
@EnableMethodSecurity
public class WebSecurityConfiguration {
  private final UserDetailsService userDetailsService;
  private final BearerTokenService tokenService;
  private final BcryptHashingService hashingService;
  private final AuthenticationEntryPoint unauthorizedRequestHandler;

  /**
   * Constructor.
   *
   * @param userDetailsService         user details service
   * @param tokenService               token service
   * @param hashingService             hashing service
   * @param unauthorizedRequestHandler unauthorized request handler
   */
  public WebSecurityConfiguration(
      @Qualifier("defaultUserDetailsService") UserDetailsService userDetailsService,
      BearerTokenService tokenService,
      BcryptHashingService hashingService,
      AuthenticationEntryPoint unauthorizedRequestHandler
  ) {
    this.userDetailsService = userDetailsService;
    this.tokenService = tokenService;
    this.hashingService = hashingService;
    this.unauthorizedRequestHandler = unauthorizedRequestHandler;
  }

  /**
   * Bean for BearerAuthorizationRequestFilter.
   *
   * @return BearerAuthorizationRequestFilter
   */
  @Bean
  public BearerAuthorizationRequestFilter authorizationRequestFilter() {
    return new BearerAuthorizationRequestFilter(tokenService, userDetailsService);
  }

  /**
   * Bean for AuthenticationManager.
   *
   * @param authenticationConfiguration authentication configuration
   * @return AuthenticationManager
   * @throws Exception if an error occurs
   */
  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  /**
   * Bean for DaoAuthenticationProvider.
   *
   * @return DaoAuthenticationProvider
   */
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    var authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    authenticationProvider.setPasswordEncoder(hashingService);
    return authenticationProvider;
  }

  /**
   * Bean for PasswordEncoder.
   *
   * @return PasswordEncoder
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return hashingService;
  }

  /**
   * Security Filter Chain.
   *
   * @param http http security
   * @return SecurityFilterChain
   * @throws Exception if an error occurs
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // CORS default configuration
    http.cors(configurer -> configurer.configurationSource(ignored -> {
      var cors = new CorsConfiguration();
      cors.setAllowedOrigins(List.of("*"));
      cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
      cors.setAllowedHeaders(List.of("*"));
      return cors;
    }));

    // CSRF disabled
    http.csrf(AbstractHttpConfigurer::disable);

    // Identity and Access Management Configuration
    http.exceptionHandling(exceptionHandling ->
            exceptionHandling.authenticationEntryPoint(unauthorizedRequestHandler))
        .sessionManagement(customizer ->
            customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
            .requestMatchers(
                "/api/v1/authentication/**",
                "/v3/api-docs/**",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/webjars/**"
            ).permitAll()
            .anyRequest().authenticated());
    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authorizationRequestFilter(),
        UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
