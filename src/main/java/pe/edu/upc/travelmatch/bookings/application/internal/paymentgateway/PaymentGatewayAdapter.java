package pe.edu.upc.travelmatch.bookings.application.internal.paymentgateway;

public interface PaymentGatewayAdapter {
    StripePaymentIntentResponse processTransaction(String paymentMethod, long amount, Long bookingId);
}
