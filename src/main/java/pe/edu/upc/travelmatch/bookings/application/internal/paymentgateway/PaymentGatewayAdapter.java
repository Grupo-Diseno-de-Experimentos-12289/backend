package pe.edu.upc.travelmatch.bookings.application.internal.paymentgateway;

/** PaymentGatewayAdapter contract. */
public interface PaymentGatewayAdapter {
  /** Process transaction. */
  StripePaymentIntentResponse processTransaction(String paymentMethod, long amount, Long bookingId);
}
