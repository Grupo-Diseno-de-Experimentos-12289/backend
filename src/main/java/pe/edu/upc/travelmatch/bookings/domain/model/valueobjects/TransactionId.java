package pe.edu.upc.travelmatch.bookings.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

/** TransactionId type. */
@Getter
@Embeddable
public class TransactionId {

  private String transactionId;

  /** Constructs a new TransactionId. */
  protected TransactionId() {}

  /** Constructs a new TransactionId. */
  public TransactionId(String transactionId) {
    if (transactionId == null || transactionId.isBlank()) {
      throw new IllegalArgumentException("TransactionId cannot be null or blank");
    }
    this.transactionId = transactionId;
  }

  @Override
  public String toString() {
    return transactionId;
  }
}
