package kr.hhplus.be.server.interfaces.coupon;

import lombok.Builder;

@Builder
public record CouponResponse (
        Long id,
        String name,
        String description,
        int discountAmount,
        String status
){
    public static CouponResponse of(long id, String name, String description, int discountAmount, String status){
        return CouponResponse.builder()
                .id(id)
                .name(name)
                .description(description)
                .discountAmount(discountAmount)
                .status(status)
                .build();
    }
}
