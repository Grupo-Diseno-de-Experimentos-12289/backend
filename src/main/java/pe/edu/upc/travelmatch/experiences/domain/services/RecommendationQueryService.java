package pe.edu.upc.travelmatch.experiences.domain.services;

import pe.edu.upc.travelmatch.experiences.domain.model.queries.CorporateRecommendation;
import pe.edu.upc.travelmatch.experiences.domain.model.queries.GetCorporateRecommendationsQuery;

import java.util.List;

/** Service to manage personalized recommendation queries. */
public interface RecommendationQueryService {
  /**
   * Handles the GetCorporateRecommendationsQuery, returning experiences at the given destination
   * that match the traveler's interests and have at least one available (in-stock) slot inside
   * their free time window, ordered by how soon they can be attended.
   *
   * @param query the query object
   * @return the list of recommendations
   */
  List<CorporateRecommendation> handle(GetCorporateRecommendationsQuery query);
}
