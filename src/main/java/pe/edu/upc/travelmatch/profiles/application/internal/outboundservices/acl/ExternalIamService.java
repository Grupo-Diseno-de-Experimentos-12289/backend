package pe.edu.upc.travelmatch.profiles.application.internal.outboundservices.acl;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.iam.interfaces.acl.IamContextFacade;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;

/** ExternalIamService type. */
@Service("profileExternalIamService")
public class ExternalIamService {

  private final IamContextFacade iamContextFacade;

  /** Constructs a new ExternalIamService. */
  public ExternalIamService(IamContextFacade iamContextFacade) {
    this.iamContextFacade = iamContextFacade;
  }

  /** Fetch user id by email. */
  public Optional<UserId> fetchUserIdByEmail(String email) {
    var userId = iamContextFacade.fetchUserIdByEmail(email);
    if (userId.equals(0L)) {
      return Optional.empty();
    }
    return Optional.of(new UserId(userId));
  }

  /** Exists user by email and id is not. */
  public boolean existsUserByEmailAndIdIsNot(String email, Long id) {
    return iamContextFacade.existsUserByEmailAndIdIsNot(email, id);
  }

  /** Exists user by id. */
  public boolean existsUserById(UserId userId) {
    return iamContextFacade.existsUserById(userId.userId());
  }

  /** Create user. */
  public Optional<UserId> createUser(
      String email,
      String password,
      String firstName,
      String lastName,
      String phone,
      List<String> roleNames) {
    var userId =
        iamContextFacade.createUser(email, password, firstName, lastName, phone, roleNames);
    if (userId.equals(0L)) {
      return Optional.empty();
    }
    return Optional.of(new UserId(userId));
  }
}
