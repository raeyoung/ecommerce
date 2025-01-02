package kr.hhplus.be.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.interfaces.user.UserBalanceRequest;
import kr.hhplus.be.server.interfaces.user.UserController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET - /api/v1/users/balance/{userId} 200 OK")
    public void balance() throws Exception {
        long userId = 1L;

        mockMvc.perform(get("/api/v1/users/balance/{userId}", userId))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("PATCH - /api/v1/users/balance/charge 200 OK")
    public void charge() throws Exception {
        UserBalanceRequest request = new UserBalanceRequest(1L, 1000000L);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(patch("/api/v1/users/balance/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
