package kr.hhplus.be.server.interfaces.payment;

public record PaymentRequest (
        Long userId,
        Long orderId
){
}
