package pe.edu.upc.travelmatch.bookings.application.internal.paymentgateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StripePaymentGatewayAdapter implements PaymentGatewayAdapter {

    public StripePaymentGatewayAdapter(@Value("${stripe.secret}") String stripeKey) {
        Stripe.apiKey = stripeKey;
    }

    @Override
    public StripePaymentIntentResponse processTransaction(String paymentMethod, long amount, Long bookingId) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("amount", amount);
            params.put("currency", "pen");
            params.put("payment_method_types", List.of(paymentMethod));

            Map<String, String> metadata = new HashMap<>();
            metadata.put("bookingId", bookingId.toString());
            params.put("metadata", metadata);

            PaymentIntent intent = PaymentIntent.create(params);
            return new StripePaymentIntentResponse(intent.getId(), intent.getClientSecret());
        } catch (StripeException e) {
            System.out.println("Stripe error: " + e.getMessage());
            return null;
        }
    }
}
