package kr.hhplus.be.server.controller;

import kr.hhplus.be.server.interfaces.coupon.CouponController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CouponController.class)
public class CouponControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST - /api/v1/coupons/issue/{userId} 200 OK")
    public void issue() throws Exception {
        long userId = 1L;

        mockMvc.perform(post("/api/v1/coupons/issue/{userId}", userId))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("GET - /api/v1/coupons/{userId} 200 OK")
    public void coupons() throws Exception {
        long userId = 1L;

        mockMvc.perform(get("/api/v1/coupons/{userId}", userId))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
