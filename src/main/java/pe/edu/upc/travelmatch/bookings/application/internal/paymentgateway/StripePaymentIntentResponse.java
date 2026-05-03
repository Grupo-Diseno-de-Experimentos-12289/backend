package pe.edu.upc.travelmatch.bookings.application.internal.paymentgateway;

public record StripePaymentIntentResponse(
        String transactionId,
        String clientSecret
) {}