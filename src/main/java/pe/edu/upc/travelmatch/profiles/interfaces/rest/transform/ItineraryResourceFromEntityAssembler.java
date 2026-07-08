package pe.edu.upc.travelmatch.profiles.interfaces.rest.transform;

import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.Itinerary;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.ItineraryActivityResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.ItineraryDayResource;
import pe.edu.upc.travelmatch.profiles.interfaces.rest.resources.ItineraryResource;

/** ItineraryResourceFromEntityAssembler type. */
public class ItineraryResourceFromEntityAssembler {
    /** To resource from entity. */
    public static ItineraryResource toResourceFromEntity(Itinerary itinerary) {
        var days =
                itinerary.days().stream()
                        .map(
                                day ->
                                        new ItineraryDayResource(
                                                day.dayNumber(),
                                                day.activities().stream()
                                                        .map(
                                                                activity ->
                                                                        new ItineraryActivityResource(
                                                                                activity.experienceId(),
                                                                                activity.title(),
                                                                                activity.category(),
                                                                                activity.meetingPoint(),
                                                                                activity.duration()))
                                                        .toList()))
                        .toList();
        return new ItineraryResource(itinerary.destinationId(), itinerary.numberOfDays(), days);
    }
}