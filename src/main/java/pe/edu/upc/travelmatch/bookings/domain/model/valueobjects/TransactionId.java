package pe.edu.upc.travelmatch.bookings.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class TransactionId {

    private String transactionId;

    protected TransactionId() {
    }

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
