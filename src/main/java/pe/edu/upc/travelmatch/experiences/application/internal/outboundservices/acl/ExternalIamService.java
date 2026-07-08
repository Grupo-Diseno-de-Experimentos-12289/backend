package pe.edu.upc.travelmatch.experiences.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.iam.interfaces.acl.IamContextFacade;

/** External IAM Service. Facade for interacting with the IAM bounded context from Experiences. */
@Service("experienceExternalIamService")
public class ExternalIamService {

  private final IamContextFacade iamContextFacade;

  /**
   * Constructor for ExternalIamService.
   *
   * @param iamContextFacade the IAM context facade
   */
  public ExternalIamService(IamContextFacade iamContextFacade) {
    this.iamContextFacade = iamContextFacade;
  }

  /**
   * Validates if an agency exists.
   *
   * @param agencyId the agency ID to validate
   */
  public void validateAgencyExists(Long agencyId) {
    if (!iamContextFacade.existsUserByRole(agencyId, "ROLE_AGENCY_STAFF")) {
      throw new IllegalArgumentException(
          "Agency with ID " + agencyId + " does not exist or is not valid.");
    }
  }

  /**
   * Validates if a tourist exists.
   *
   * @param userId the tourist ID to validate
   */
  public void validateTouristExists(Long userId) {
    if (!iamContextFacade.existsUserByRole(userId, "ROLE_TOURIST")) {
      throw new IllegalArgumentException(
          "Tourist with ID " + userId + " does not exist or is not valid.");
    }
  }
}
