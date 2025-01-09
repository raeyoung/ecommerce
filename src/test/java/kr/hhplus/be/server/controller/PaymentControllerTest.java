package kr.hhplus.be.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.interfaces.payment.PaymentController;
import kr.hhplus.be.server.interfaces.payment.PaymentRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST - /api/v1/payments 200 OK")
    public void payment() throws Exception {
        PaymentRequest request = new PaymentRequest(1L, 1L);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
