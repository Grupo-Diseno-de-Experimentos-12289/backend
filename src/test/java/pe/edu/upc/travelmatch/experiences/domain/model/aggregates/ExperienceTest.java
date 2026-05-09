package pe.edu.upc.travelmatch.experiences.domain.model.aggregates;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.AgencyId;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.DestinationId;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ExperienceTest {

    @Mock
    private Category category;

    @Mock
    private Category anotherCategory;

    @Mock
    private AgencyId agencyId;

    @Mock
    private DestinationId destinationId;

    @Mock
    private DestinationId anotherDestinationId;

    @Test
    void testExperienceConstructorAndGetters() {
        // Arrange
        String title = "Machu Picchu Tour";
        String description = "Full day tour to the Inca citadel";
        String duration = "12 hours";
        String meetingPoint = "Main Square Cusco";

        // Act
        Experience experience = new Experience(title, description, agencyId, category, destinationId, duration, meetingPoint);

        // Assert
        assertEquals(title, experience.getTitle());
        assertEquals(description, experience.getDescription());
        assertEquals(agencyId, experience.getAgencyId());
        assertEquals(category, experience.getCategory());
        assertEquals(destinationId, experience.getDestinationId());
        assertEquals(duration, experience.getDuration());
        assertEquals(meetingPoint, experience.getMeetingPoint());
        assertTrue(experience.getAvailabilities().isEmpty());
        assertTrue(experience.getMedia().isEmpty());
    }

    @Test
    void testUpdateInfo_Successfully() {
        // Arrange
        Experience experience = new Experience("Old Title", "Old Desc", agencyId, category, destinationId, "1h", "Point A");
        String newTitle = "New Title";
        String newDescription = "New Description updated";
        String newDuration = "2h";
        String newMeetingPoint = "Point B";

        // Act
        experience.updateInfo(newTitle, newDescription, anotherCategory, anotherDestinationId, newDuration, newMeetingPoint);

        // Assert
        assertEquals(newTitle, experience.getTitle());
        assertEquals(newDescription, experience.getDescription());
        assertEquals(anotherCategory, experience.getCategory());
        assertEquals(anotherDestinationId, experience.getDestinationId());
        assertEquals(newDuration, experience.getDuration());
        assertEquals(newMeetingPoint, experience.getMeetingPoint());
    }

    @Test
    void testMarkAsDeleted_Successfully() {
        // Arrange
        Experience experience = new Experience("Title", "Desc", agencyId, category, destinationId, "1h", "Point A");

        // Act
        experience.markAsDeleted();

        // Assert
        assertNotNull(experience.getDeletedAt());
    }

    @Test
    void testNoArgsConstructor_InitializesLists() {
        // Act
        Experience experience = new Experience();

        // Assert
        assertNotNull(experience.getAvailabilities());
        assertNotNull(experience.getMedia());
        assertTrue(experience.getAvailabilities().isEmpty());
        assertTrue(experience.getMedia().isEmpty());
    }
    
}
