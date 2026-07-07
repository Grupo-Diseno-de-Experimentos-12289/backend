package pe.edu.upc.travelmatch.bookings.interfaces.rest.transform;

import pe.edu.upc.travelmatch.bookings.domain.model.entities.Refund;
import pe.edu.upc.travelmatch.bookings.interfaces.rest.resources.RefundResource;

/** RefundResourceFromEntityAssembler type. */
public class RefundResourceFromEntityAssembler {
  /** To resource from entity. */
  public static RefundResource toResourceFromEntity(Refund refund) {
    return new RefundResource(
        refund.getId(),
        refund.getBooking().getId(),
        refund.getRefundMoney().getAmount(),
        refund.getRefundMoney().getCurrency(),
        refund.getRefundStatus().name(),
        refund.getRefundReason(),
        refund.getRefundDate());
  }
}
