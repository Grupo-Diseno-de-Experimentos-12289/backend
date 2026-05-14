package pe.edu.upc.travelmatch.bookings.application.internal.paymentgateway;

/** StripePaymentIntentResponse value carrier. */
public record StripePaymentIntentResponse(String transactionId, String clientSecret) {}
