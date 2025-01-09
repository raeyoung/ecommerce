package kr.hhplus.be.server.interfaces.payment;

import kr.hhplus.be.server.interfaces.product.ProductResponse;
import lombok.Builder;

@Builder
public record PaymentResponse (
   Long id,
   int amount,

   String status

){
    public static PaymentResponse of(long id, int paymentAmount, String status) {
        return PaymentResponse.builder()
                .id(id)
                .amount(paymentAmount)
                .status(status)
                .build();
    }
}
