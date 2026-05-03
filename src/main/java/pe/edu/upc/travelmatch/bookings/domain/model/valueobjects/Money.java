package pe.edu.upc.travelmatch.bookings.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Money Value Object representing a monetary amount with currency.
 * This class ensures type safety and consistency when handling monetary values.
 * Implements Domain-Driven Design (DDD) principles as an embeddable value object.
 */
@Getter
@Embeddable
public class Money {

    /**
     * The monetary amount with precision of 10 total digits and 2 decimal places.
     * Uses BigDecimal to avoid floating-point precision issues.
     */
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /**
     * The currency code following ISO 4217 standard (e.g., "PEN", "USD", "EUR").
     * Limited to 3 characters as per ISO specification.
     */
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    /**
     * Main constructor for creating a Money object.
     *
     * @param amount   The monetary amount (must be non-negative)
     * @param currency The ISO 4217 currency code (must be 3 uppercase letters)
     * @throws IllegalArgumentException if amount is negative or currency format is invalid
     * @throws NullPointerException     if amount or currency is null
     */
    public Money(BigDecimal amount, String currency) {
        Objects.requireNonNull(amount, "Money amount cannot be null");
        Objects.requireNonNull(currency, "Money currency cannot be null");

        validateAmount(amount);
        validateCurrency(currency);

        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency.toUpperCase();
    }

    /**
     * Default constructor for JPA/Hibernate.
     * Creates a Money object with zero amount in PEN currency.
     */
    protected Money() {
        this.amount = BigDecimal.ZERO;
        this.currency = "PEN";
    }

    /**
     * Validates that the amount is non-negative.
     *
     * @param amount The amount to validate
     * @throws IllegalArgumentException if amount is negative
     */
    private void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Money amount cannot be negative");
        }
    }

    /**
     * Validates that the currency follows ISO 4217 format.
     *
     * @param currency The currency code to validate
     * @throws IllegalArgumentException if currency format is invalid
     */
    private void validateCurrency(String currency) {
        if (currency.length() != 3 || !currency.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Invalid currency format. Must be a 3-letter ISO code.");
        }
    }

    /**
     * Adds another Money object to this one.
     * Both Money objects must have the same currency.
     *
     * @param other The Money object to add
     * @return A new Money object with the sum of both amounts
     * @throws IllegalArgumentException if currencies don't match
     * @throws NullPointerException     if other is null
     */
    public Money add(Money other) {
        Objects.requireNonNull(other, "Cannot add null Money object");
        validateSameCurrency(other, "Cannot add Money objects with different currencies.");

        return new Money(this.amount.add(other.amount), this.currency);
    }

    /**
     * Subtracts another Money object from this one.
     * Both Money objects must have the same currency.
     *
     * @param other The Money object to subtract
     * @return A new Money object with the difference of both amounts
     * @throws IllegalArgumentException if currencies don't match
     * @throws NullPointerException     if other is null
     */
    public Money subtract(Money other) {
        Objects.requireNonNull(other, "Cannot subtract null Money object");
        validateSameCurrency(other, "Cannot subtract Money objects with different currencies.");

        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    /**
     * Multiplies this Money by a given multiplier.
     *
     * @param multiplier The value to multiply by
     * @return A new Money object with the multiplied amount
     * @throws NullPointerException if multiplier is null
     */
    public Money multiply(BigDecimal multiplier) {
        Objects.requireNonNull(multiplier, "Multiplier cannot be null");
        return new Money(this.amount.multiply(multiplier), this.currency);
    }

    /**
     * Checks if this Money is greater than another Money object.
     * Both Money objects must have the same currency.
     *
     * @param other The Money object to compare with
     * @return true if this Money is greater than the other
     * @throws IllegalArgumentException if currencies don't match
     * @throws NullPointerException     if other is null
     */
    public boolean isGreaterThan(Money other) {
        Objects.requireNonNull(other, "Cannot compare with null Money object");
        validateSameCurrency(other, "Cannot compare Money objects with different currencies.");

        return this.amount.compareTo(other.amount) > 0;
    }

    /**
     * Checks if this Money is less than another Money object.
     * Both Money objects must have the same currency.
     *
     * @param other The Money object to compare with
     * @return true if this Money is less than the other
     * @throws IllegalArgumentException if currencies don't match
     * @throws NullPointerException     if other is null
     */
    public boolean isLessThan(Money other) {
        Objects.requireNonNull(other, "Cannot compare with null Money object");
        validateSameCurrency(other, "Cannot compare Money objects with different currencies.");

        return this.amount.compareTo(other.amount) < 0;
    }

    /**
     * Checks if two Money objects have the same currency.
     *
     * @param other The Money object to check currency with
     * @return true if both Money objects have the same currency
     * @throws NullPointerException if other is null
     */
    public boolean isSameCurrency(Money other) {
        Objects.requireNonNull(other, "Cannot check currency with null Money object");
        return this.currency.equals(other.currency);
    }

    /**
     * Validates that two Money objects have the same currency.
     *
     * @param other   The Money object to compare currency with
     * @param message The error message to throw if currencies don't match
     * @throws IllegalArgumentException if currencies don't match
     */
    private void validateSameCurrency(Money other, String message) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Compares this Money object with another for equality.
     * Two Money objects are equal if they have the same amount and currency.
     *
     * @param obj The object to compare with
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Money money = (Money) obj;
        return amount.compareTo(money.amount) == 0 &&
                Objects.equals(currency, money.currency);
    }

    /**
     * Returns a hash code for this Money object.
     *
     * @return hash code based on amount and currency
     */
    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    /**
     * Returns a string representation of this Money object.
     *
     * @return formatted string showing amount and currency (e.g., "100.50 USD")
     */
    @Override
    public String toString() {
        return amount.toPlainString() + " " + currency;
    }
}