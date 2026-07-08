package pe.edu.upc.travelmatch.experiences.domain.model.aggregates;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.experiences.domain.model.entities.Category;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.AgencyId;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.CancellationPolicyType;
import pe.edu.upc.travelmatch.experiences.domain.model.valueobjects.DestinationId;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExperienceTest {

  @Mock private Category category;

  @Mock private Category anotherCategory;

  @Mock private AgencyId agencyId;

  @Mock private DestinationId destinationId;

  @Mock private DestinationId anotherDestinationId;

  @Test
  void testExperienceConstructorAndGetters() {
    // Arrange
    String title = "Machu Picchu Tour";
    String description = "Full day tour to the Inca citadel";
    String duration = "12 hours";
    String meetingPoint = "Main Square Cusco";
    String cancellationPolicyDescription = "Free cancellation up to 24 hours before the tour.";

    // Act
    Experience experience =
        new Experience(
            title,
            description,
            agencyId,
            category,
            destinationId,
            duration,
            meetingPoint,
            CancellationPolicyType.FLEXIBLE,
            cancellationPolicyDescription);

    // Assert
    assertEquals(title, experience.getTitle());
    assertEquals(description, experience.getDescription());
    assertEquals(agencyId, experience.getAgencyId());
    assertEquals(category, experience.getCategory());
    assertEquals(destinationId, experience.getDestinationId());
    assertEquals(duration, experience.getDuration());
    assertEquals(meetingPoint, experience.getMeetingPoint());
    assertEquals(CancellationPolicyType.FLEXIBLE, experience.getCancellationPolicyType());
    assertEquals(cancellationPolicyDescription, experience.getCancellationPolicyDescription());
    assertTrue(experience.getAvailabilities().isEmpty());
    assertTrue(experience.getMedia().isEmpty());
  }

  @Test
  void testExperienceConstructor_DefaultsCancellationPolicyToFlexibleWhenNull() {
    // Act
    Experience experience =
        new Experience(
            "Title", "Desc", agencyId, category, destinationId, "1h", "Point A", null, null);

    // Assert
    assertEquals(CancellationPolicyType.FLEXIBLE, experience.getCancellationPolicyType());
  }

  @Test
  void testUpdateInfo_Successfully() {
    // Arrange
    Experience experience =
        new Experience(
            "Old Title",
            "Old Desc",
            agencyId,
            category,
            destinationId,
            "1h",
            "Point A",
            CancellationPolicyType.FLEXIBLE,
            "Old policy");
    String newTitle = "New Title";
    String newDescription = "New Description updated";
    String newDuration = "2h";
    String newMeetingPoint = "Point B";
    String newCancellationPolicyDescription = "New policy: 50% refund up to 5 days before.";

    // Act
    experience.updateInfo(
        newTitle,
        newDescription,
        anotherCategory,
        anotherDestinationId,
        newDuration,
        newMeetingPoint,
        CancellationPolicyType.MODERATE,
        newCancellationPolicyDescription);

    // Assert
    assertEquals(newTitle, experience.getTitle());
    assertEquals(newDescription, experience.getDescription());
    assertEquals(anotherCategory, experience.getCategory());
    assertEquals(anotherDestinationId, experience.getDestinationId());
    assertEquals(newDuration, experience.getDuration());
    assertEquals(newMeetingPoint, experience.getMeetingPoint());
    assertEquals(CancellationPolicyType.MODERATE, experience.getCancellationPolicyType());
    assertEquals(newCancellationPolicyDescription, experience.getCancellationPolicyDescription());
  }

  @Test
  void testUpdateInfo_KeepsPreviousPolicyTypeWhenNullIsPassed() {
    // Arrange
    Experience experience =
        new Experience(
            "Old Title",
            "Old Desc",
            agencyId,
            category,
            destinationId,
            "1h",
            "Point A",
            CancellationPolicyType.STRICT,
            "Old policy");

    // Act
    experience.updateInfo(
        "New Title",
        "New Desc",
        anotherCategory,
        anotherDestinationId,
        "2h",
        "Point B",
        null,
        "Updated description only");

    // Assert
    assertEquals(CancellationPolicyType.STRICT, experience.getCancellationPolicyType());
    assertEquals("Updated description only", experience.getCancellationPolicyDescription());
  }

  @Test
  void testMarkAsDeleted_Successfully() {
    // Arrange
    Experience experience =
        new Experience(
            "Title",
            "Desc",
            agencyId,
            category,
            destinationId,
            "1h",
            "Point A",
            CancellationPolicyType.FLEXIBLE,
            "Policy");

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
