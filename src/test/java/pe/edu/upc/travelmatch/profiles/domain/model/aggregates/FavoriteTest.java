package pe.edu.upc.travelmatch.profiles.domain.model.aggregates;

import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;

import static org.junit.jupiter.api.Assertions.*;

class FavoriteTest {

    @Test
    void testFavoriteConstructorAndGetters() {
        // Arrange
        UserId userId = new UserId(1L);
        ExperienceId experienceId = new ExperienceId(10L);

        // Act
        Favorite favorite = new Favorite(userId, experienceId);

        // Assert
        assertEquals(userId, favorite.getUserId());
        assertEquals(experienceId, favorite.getExperienceId());
    }

    @Test
    void testBelongsTo_ReturnsTrueForMatchingUserId() {
        // Arrange
        UserId userId = new UserId(1L);
        ExperienceId experienceId = new ExperienceId(10L);
        Favorite favorite = new Favorite(userId, experienceId);

        // Act & Assert
        assertTrue(favorite.belongsTo(new UserId(1L)));
    }

    @Test
    void testBelongsTo_ReturnsFalseForDifferentUserId() {
        // Arrange
        UserId userId = new UserId(1L);
        ExperienceId experienceId = new ExperienceId(10L);
        Favorite favorite = new Favorite(userId, experienceId);

        // Act & Assert
        assertFalse(favorite.belongsTo(new UserId(2L)));
    }
}
