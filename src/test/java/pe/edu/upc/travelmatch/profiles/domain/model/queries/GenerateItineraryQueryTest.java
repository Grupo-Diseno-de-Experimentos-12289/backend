package pe.edu.upc.travelmatch.profiles.domain.model.queries;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GenerateItineraryQueryTest {

  @Test
  @DisplayName("constructor stores fields correctly when numberOfDays is valid")
  void constructor_validNumberOfDays_storesFields() {
    var query = new GenerateItineraryQuery(1L, List.of("CULTURA"), 3);

    assertEquals(1L, query.destinationId());
    assertEquals(List.of("CULTURA"), query.categories());
    assertEquals(3, query.numberOfDays());
  }

  @Test
  @DisplayName("constructor replaces null categories with an empty list")
  void constructor_nullCategories_replacedWithEmptyList() {
    var query = new GenerateItineraryQuery(1L, null, 2);

    assertTrue(query.categories().isEmpty());
  }

  @Test
  @DisplayName("constructor throws IllegalArgumentException when numberOfDays is zero")
  void constructor_zeroNumberOfDays_throwsIllegalArgument() {
    assertThrows(
        IllegalArgumentException.class, () -> new GenerateItineraryQuery(1L, List.of(), 0));
  }
}