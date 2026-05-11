package pe.edu.upc.travelmatch.experiences.domain.model.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.experiences.domain.model.aggregates.Experience;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExperienceMediaTest {

    @Mock
    private Experience experience;

    @Test
    void testExperienceMediaConstructorAndGetters() {
        // Arrange
        String mediaUrl = "https://example.com/image.jpg";
        String caption = "Vista panorámica";

        // Act
        ExperienceMedia media = new ExperienceMedia(experience, mediaUrl, caption);

        // Assert
        assertEquals(experience, media.getExperience());
        assertEquals(mediaUrl, media.getMediaUrl());
        assertEquals(caption, media.getCaption());
    }

    @Test
    void testUpdate_Successfully() {
        // Arrange
        ExperienceMedia media = new ExperienceMedia(experience, "old-url.com", "Old Caption");
        String newUrl = "new-url.com";
        String newCaption = "New Caption";

        // Act
        media.update(newUrl, newCaption);

        // Assert
        assertEquals(newUrl, media.getMediaUrl());
        assertEquals(newCaption, media.getCaption());
    }

    @Test
    void testMarkAsDeleted_Successfully() {
        // Arrange
        ExperienceMedia media = new ExperienceMedia(experience, "url.com", "Caption");

        // Act
        media.markAsDeleted();

        // Assert
        assertNotNull(media.getDeletedAt());
    }

    @Test
    void testNoArgsConstructor_InitializesCorrectly() {
        // Act
        ExperienceMedia media = new ExperienceMedia();

        // Assert
        assertNull(media.getExperience());
        assertNull(media.getMediaUrl());
        assertNull(media.getDeletedAt());
    }
}