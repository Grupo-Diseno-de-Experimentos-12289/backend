package pe.edu.upc.travelmatch.bookings.interfaces.rest;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.travelmatch.bookings.application.internal.commandservices.BookingCommandServiceImpl;
import pe.edu.upc.travelmatch.bookings.domain.model.commands.ProcessPaymentCommand;
import pe.edu.upc.travelmatch.bookings.domain.model.valueobjects.TransactionId;

@RestController
@RequestMapping("api/v1/stripe/webhook")
@RequiredArgsConstructor
public class StripeWebhookController {

    private final BookingCommandServiceImpl bookingCommandService;

    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;

    private static final String STRIPE_SIGNATURE_HEADER = "Stripe-Signature";

    @PostMapping
    @Transactional
    public ResponseEntity<String> handleStripeWebhook(
            @RequestHeader(STRIPE_SIGNATURE_HEADER) String sigHeader,
            @RequestBody String payload
    ) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);

            if ("payment_intent.succeeded".equals(event.getType())) {
                PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject().orElse(null);

                if (intent != null) {
                    String transactionId = intent.getId();
                    Long bookingId = Long.valueOf(intent.getMetadata().get("bookingId"));

                    bookingCommandService.handle(
                            new ProcessPaymentCommand(bookingId, intent.getPaymentMethod(), new TransactionId(transactionId))
                    );
                }
            }

            return ResponseEntity.ok().build();

        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(400).body("Invalid signature");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Webhook error: " + ex.getMessage());
        }
    }
}
