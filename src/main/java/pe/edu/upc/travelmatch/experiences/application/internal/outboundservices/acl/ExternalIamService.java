package pe.edu.upc.travelmatch.experiences.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.iam.interfaces.acl.IamContextFacade;

@Service("experienceExternalIamService")
public class ExternalIamService {

    private final IamContextFacade iamContextFacade;

    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    public void validateAgencyExists(Long agencyId) {
        if (!iamContextFacade.existsUserByRole(agencyId, "ROLE_AGENCY_STAFF")) {
            throw new IllegalArgumentException("Agency with ID " + agencyId + " does not exist or is not valid.");
        }
    }

    public void validateTouristExists(Long userId) {
        if (!iamContextFacade.existsUserByRole(userId, "ROLE_TOURIST")) {
            throw new IllegalArgumentException("Tourist with ID " + userId + " does not exist or is not valid.");
        }
    }
}