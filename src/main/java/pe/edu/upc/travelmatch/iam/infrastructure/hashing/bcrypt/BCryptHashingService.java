package pe.edu.upc.travelmatch.iam.infrastructure.hashing.bcrypt;

import org.springframework.security.crypto.password.PasswordEncoder;
import pe.edu.upc.travelmatch.iam.application.internal.outboundservices.hashing.HashingService;

/** BCryptHashingService contract. */
public interface BCryptHashingService extends HashingService, PasswordEncoder {}
