package pe.edu.upc.travelmatch.experiences.interfaces.acl.dto;

import java.time.LocalDateTime;
import java.util.List;

public record AvailabilityInfo(
        Long availabilityId,
        Long experienceId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        List<AvailabilityTicketTypeInfo> ticketTypes
) {}