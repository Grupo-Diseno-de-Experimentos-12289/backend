package pe.edu.upc.travelmatch.iam.infrastructure.hashing.bcrypt;

import org.springframework.security.crypto.password.PasswordEncoder;
import pe.edu.upc.travelmatch.iam.application.internal.outboundservices.hashing.HashingService;

/**
 * Bcrypt hashing service interface.
 */
public interface BcryptHashingService extends HashingService, PasswordEncoder {
}
