package pe.edu.upc.travelmatch.profiles.application.internal.queryservices;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import pe.edu.upc.travelmatch.profiles.application.internal.outboundservices.acl.ExternalExperienceService;
import pe.edu.upc.travelmatch.profiles.domain.model.queries.GenerateItineraryQuery;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Itinerary;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ItineraryActivity;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ItineraryDay;
import pe.edu.upc.travelmatch.profiles.domain.services.ItineraryQueryService;

/** Service implementation for generating personalized itineraries. */
@Service
public class ItineraryQueryServiceImpl implements ItineraryQueryService {

    private final ExternalExperienceService externalExperienceService;

    /**
     * Constructs an ItineraryQueryServiceImpl.
     *
     * @param externalExperienceService the ACL service to the Experiences bounded context
     */
    public ItineraryQueryServiceImpl(ExternalExperienceService externalExperienceService) {
        this.externalExperienceService = externalExperienceService;
    }

    @Override
    public Itinerary handle(GenerateItineraryQuery query) {
        var experiences =
                externalExperienceService.fetchExperiencesByDestinationAndCategories(
                        query.destinationId(), query.categories());

        if (experiences.isEmpty()) {
            throw new IllegalStateException(
                    "No experiences were found matching the given destination and preferences.");
        }

        Map<Integer, List<ItineraryActivity>> activitiesByDay = new LinkedHashMap<>();
        for (int day = 1; day <= query.numberOfDays(); day++) {
            activitiesByDay.put(day, new ArrayList<>());
        }

        for (int i = 0; i < experiences.size(); i++) {
            var experience = experiences.get(i);
            int day = (i % query.numberOfDays()) + 1;
            activitiesByDay
                    .get(day)
                    .add(
                            new ItineraryActivity(
                                    experience.experienceId(),
                                    experience.title(),
                                    experience.category(),
                                    experience.meetingPoint(),
                                    experience.duration()));
        }

        List<ItineraryDay> days =
                activitiesByDay.entrySet().stream()
                        .map(entry -> new ItineraryDay(entry.getKey(), entry.getValue()))
                        .toList();

        return new Itinerary(query.destinationId(), query.numberOfDays(), days);
    }
}