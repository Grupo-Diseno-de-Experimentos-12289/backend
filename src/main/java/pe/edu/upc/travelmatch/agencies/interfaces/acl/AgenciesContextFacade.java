package pe.edu.upc.travelmatch.agencies.interfaces.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.iam.domain.model.queries.GetUserByIdQuery;
import pe.edu.upc.travelmatch.iam.domain.services.UserQueryService;

@Service
public class AgenciesContextFacade {

    private final UserQueryService userQueryService;

    public AgenciesContextFacade(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    public boolean existsUserById(Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        return userQueryService.handle(getUserByIdQuery).isPresent();
    }
}