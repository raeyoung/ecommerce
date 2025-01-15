package kr.hhplus.be.server.integration.coupon;

import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.facade.coupon.CouponFacade;
import kr.hhplus.be.server.interfaces.coupon.CouponController;
import kr.hhplus.be.server.interfaces.coupon.IssuedCouponResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class CouponControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CouponController couponController;

    @Mock
    private CouponFacade couponFacade;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // MockMvc 객체 생성
        this.mockMvc = MockMvcBuilders.standaloneSetup(couponController).build();
    }

    @Test
    @DisplayName("/api/v1/coupons/issue/{userId} 200 OK")
    void issueCoupon() throws Exception {
        // Given
        long userId = 1L;
        long couponId = 3L;

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/coupons/issue/{userId}", userId)
                        .param("couponId", String.valueOf(couponId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("/api/v1/coupons/user/{userId} 200 OK")
    void coupons() throws Exception {
        // Given
        long userId = 1L;
        // 샘플 데이터 생성
        IssuedCouponResponse coupon1 = new IssuedCouponResponse(1L, 1L, CouponStatus.AVAILABLE,
                LocalDateTime.now(), LocalDateTime.now().plusDays(7));
        IssuedCouponResponse coupon2 = new IssuedCouponResponse(2L, 2L, CouponStatus.AVAILABLE,
                LocalDateTime.now(), LocalDateTime.now().plusDays(7));

        List<IssuedCouponResponse> coupons = List.of(coupon1, coupon2);
        Page<IssuedCouponResponse> issuedCouponResponses = new PageImpl<>(coupons, PageRequest.of(0, 10), coupons.size());

        // couponFacade의 userCoupons 메서드가 반환할 결과를 Mock 설정
        when(couponFacade.getIssuedCoupons(userId, 0, 10)).thenReturn(issuedCouponResponses);

        // When & Then
        mockMvc.perform(get("/api/v1/coupons/user/{userId}", userId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // HTTP 상태 200 OK
                .andExpect(jsonPath("$.content[0].couponId").value(1L))  // 첫 번째 쿠폰의 couponId가 1L이어야 한다
                .andExpect(jsonPath("$.content[1].couponId").value(2L))  // 두 번째 쿠폰의 couponId가 2L이어야 한다
                .andExpect(jsonPath("$.content[0].status").value("AVAILABLE"))  // 첫 번째 쿠폰의 상태가 "AVAILABLE"이어야 한다
                .andExpect(jsonPath("$.content[1].status").value("AVAILABLE"));  // 두 번째 쿠폰의 상태가 "AVAILABLE"이어야 한다
    }
}
