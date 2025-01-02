package kr.hhplus.be.server.controller;

import kr.hhplus.be.server.interfaces.product.ProductController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET - /api/v1/products 200 OK")
    public void products() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("GET - /api/v1/products/top 200 OK")
    public void topProducts() throws Exception {
        mockMvc.perform(get("/api/v1/products/top"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
