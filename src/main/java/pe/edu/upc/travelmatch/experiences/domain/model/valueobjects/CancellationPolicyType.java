package pe.edu.upc.travelmatch.experiences.domain.model.valueobjects;

/** Enum representing the type of cancellation policy applied to an Experience. */
public enum CancellationPolicyType {
  /** Full refund if cancelled with enough anticipation (e.g. 24h before). */
  FLEXIBLE,
  /** Partial refund if cancelled with moderate anticipation (e.g. 5 days before). */
  MODERATE,
  /** Refund only under strict conditions (e.g. 7+ days before, fees may apply). */
  STRICT,
  /** No refund is granted once the booking is confirmed. */
  NON_REFUNDABLE
}
