package pe.edu.upc.travelmatch.profiles.domain.model.aggregates;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.ExperienceId;
import pe.edu.upc.travelmatch.profiles.domain.model.valueobjects.UserId;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FavoriteTest {

    @Mock
    private UserId userId;

    @Mock
    private UserId otherUserId;

    @Mock
    private ExperienceId experienceId;

    @Test
    void testFavoriteConstructorAndGetters() {
        // Act
        Favorite favorite = new Favorite(userId, experienceId);

        // Assert
        assertEquals(userId, favorite.getUserId());
        assertEquals(experienceId, favorite.getExperienceId());
    }

    @Test
    void testBelongsTo_ReturnsTrueForMatchingUserId() {
        // Arrange
        Favorite favorite = new Favorite(userId, experienceId);

        // Act & Assert
        assertTrue(favorite.belongsTo(userId));
    }

    @Test
    void testBelongsTo_ReturnsFalseForDifferentUserId() {
        // Arrange
        Favorite favorite = new Favorite(userId, experienceId);

        // Act & Assert
        assertFalse(favorite.belongsTo(otherUserId));
    }
}
