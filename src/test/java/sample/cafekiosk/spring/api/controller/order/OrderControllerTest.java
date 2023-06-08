package sample.cafekiosk.spring.api.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    OrderService orderService;
    @MockBean
    ProductRepository productRepository;
    @Autowired
    ObjectMapper om;

    @Test
    @DisplayName("상품번호를 받아 주문을 생성한다.")
    void createOrder() throws Exception {
        // given
        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001"))
                .build();

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/order/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(OK.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(OK.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
        ;
    }

    @Test
    @DisplayName("주문번호를 1개 이상을 받아 주문을 생성한다.")
    void createOrderNoProductNumbers() throws Exception {
        // given
        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of())
                .build();

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/order/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("상품번호는 필수 입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
        ;
    }
}