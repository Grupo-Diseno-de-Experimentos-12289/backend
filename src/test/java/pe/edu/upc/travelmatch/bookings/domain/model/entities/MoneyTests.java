package pe.edu.upc.travelmatch.bookings.domain.model.entities;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.Money;

@Nested
@DisplayName("Money Value Object")
class MoneyTests {

  @Test
  @DisplayName("Money() with valid amount and currency creates instance correctly")
  void constructor_validData_createsInstance() {
    // Arrange
    BigDecimal amount = new BigDecimal("99.50");
    String currency = "PEN";

    // Act
    Money money = new Money(amount, currency);

    // Assert
    assertAll(
        () -> assertEquals(new BigDecimal("99.50"), money.getAmount()),
        () -> assertEquals("PEN", money.getCurrency()));
  }

  @Test
  @DisplayName("Money() throws NullPointerException when amount is null")
  void constructor_nullAmount_throwsNullPointer() {
    // Arrange – null amount

    // Act & Assert
    assertThrows(NullPointerException.class, () -> new Money(null, "PEN"));
  }

  @Test
  @DisplayName("Money() throws NullPointerException when currency is null")
  void constructor_nullCurrency_throwsNullPointer() {
    // Arrange – null currency

    // Act & Assert
    assertThrows(NullPointerException.class, () -> new Money(BigDecimal.TEN, null));
  }

  @Test
  @DisplayName("Money() throws IllegalArgumentException when amount is negative")
  void constructor_negativeAmount_throwsIllegalArgument() {
    // Arrange
    BigDecimal negativeAmount = new BigDecimal("-1.00");

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> new Money(negativeAmount, "PEN"));
  }

  @Test
  @DisplayName("Money() throws IllegalArgumentException when currency is not 3 uppercase letters")
  void constructor_invalidCurrency_throwsIllegalArgument() {
    // Arrange
    String invalidCurrency = "pe";

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> new Money(BigDecimal.TEN, invalidCurrency));
  }

  @Test
  @DisplayName("add() returns sum of two Money objects with same currency")
  void add_sameCurrency_returnsCorrectSum() {
    // Arrange
    Money a = new Money(new BigDecimal("50.00"), "PEN");
    Money b = new Money(new BigDecimal("30.00"), "PEN");

    // Act
    Money result = a.add(b);

    // Assert
    assertEquals(new BigDecimal("80.00"), result.getAmount());
  }

  @Test
  @DisplayName("add() throws IllegalArgumentException when currencies differ")
  void add_differentCurrencies_throwsIllegalArgument() {
    // Arrange
    Money pen = new Money(new BigDecimal("50.00"), "PEN");
    Money usd = new Money(new BigDecimal("30.00"), "USD");

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> pen.add(usd));
  }

  @Test
  @DisplayName("subtract() returns difference of two Money objects")
  void subtract_sameCurrency_returnsCorrectDifference() {
    // Arrange
    Money a = new Money(new BigDecimal("100.00"), "PEN");
    Money b = new Money(new BigDecimal("40.00"), "PEN");

    // Act
    Money result = a.subtract(b);

    // Assert
    assertEquals(new BigDecimal("60.00"), result.getAmount());
  }

  @Test
  @DisplayName("multiply() returns correct product")
  void multiply_validMultiplier_returnsProduct() {
    // Arrange
    Money money = new Money(new BigDecimal("25.00"), "PEN");

    // Act
    Money result = money.multiply(new BigDecimal("4"));

    // Assert
    assertEquals(new BigDecimal("100.00"), result.getAmount());
  }

  @Test
  @DisplayName("isGreaterThan() returns true when amount is larger")
  void isGreaterThan_largerAmount_returnsTrue() {
    // Arrange
    Money bigger = new Money(new BigDecimal("200.00"), "PEN");
    Money smaller = new Money(new BigDecimal("50.00"), "PEN");

    // Act
    boolean result = bigger.isGreaterThan(smaller);

    // Assert
    assertTrue(result);
  }

  @Test
  @DisplayName("equals() returns true when amount and currency match")
  void equals_sameAmountAndCurrency_returnsTrue() {
    // Arrange
    Money a = new Money(new BigDecimal("100.00"), "PEN");
    Money b = new Money(new BigDecimal("100.00"), "PEN");

    // Act & Assert
    assertEquals(a, b);
  }

  @Test
  @DisplayName("toString() includes amount and currency string")
  void toString_containsAmountAndCurrency() {
    // Arrange
    Money money = new Money(new BigDecimal("50.00"), "PEN");

    // Act
    String result = money.toString();

    // Assert
    assertAll(() -> assertTrue(result.contains("50.00")), () -> assertTrue(result.contains("PEN")));
  }
}
