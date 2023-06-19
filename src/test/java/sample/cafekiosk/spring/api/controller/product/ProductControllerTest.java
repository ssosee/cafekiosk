package sample.cafekiosk.spring.api.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sample.cafekiosk.spring.ControllerTestSupport;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


class ProductControllerTest extends ControllerTestSupport {

   @Test
   @DisplayName("신규 상품을 등록한다.")
   void createProduct() throws Exception {
       // given
       ProductCreateRequest request = ProductCreateRequest.builder()
               .productType(ProductType.HANDMADE)
               .productSellingType(ProductSellingType.SELLING)
               .name("아메리카노")
               .price(4000)
               .build();

       // when // then
       mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                       .content(om.writeValueAsString(request))
                       .contentType(MediaType.APPLICATION_JSON))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isOk());
   }

    @Test
    @DisplayName("신규 상품을 등록할 때 상품타입은 필수 입니다.")
    void validationProductType() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .productSellingType(ProductSellingType.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(om.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("상품 타입은 필수 입니다."))
                .andExpect(jsonPath("$.data").isEmpty())
                ;
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 상품 판매 타입은 필수 입니다.")
    void validationProductSellingType() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .productType(ProductType.HANDMADE)
                .name("아메리카노")
                .price(4000)
                .build();

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(om.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("상품 판매 타입은 필수 입니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 상품명은 필수 입니다.")
    void validationProductName() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .productType(ProductType.HANDMADE)
                .productSellingType(ProductSellingType.SELLING)
                .price(4000)
                .build();

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(om.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("상품명은 필수 입니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 상품타입은 필수 입니다. (상품 가격이 0인 경우)")
    void validationProductPriceIsZero() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .productType(ProductType.HANDMADE)
                .productSellingType(ProductSellingType.SELLING)
                .name("아메리카노")
                .price(0)
                .build();

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(om.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("상품 가격은 0원 이상이어야 합니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 상품가격은 0원 이상 입니다. (상품가격이 음수인 경우)")
    void validationProductPriceIsNegative() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .productType(ProductType.HANDMADE)
                .productSellingType(ProductSellingType.SELLING)
                .name("아메리카노")
                .price(-1)
                .build();

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(om.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("상품 가격은 0원 이상이어야 합니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @Test
    @DisplayName("신규 상품을 등록할 때 상품가격은 0원 이상 입니다. (상품가격이 없는 경우)")
    void validationProductPriceIsEmpty() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .productType(ProductType.HANDMADE)
                .productSellingType(ProductSellingType.SELLING)
                .name("아메리카노")
                .build();

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                        .content(om.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value("상품 가격은 0원 이상이어야 합니다."))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @Test
    @DisplayName("판매 상품을 조회한다.")
    void getSellingProduct() throws Exception {
        // given
        List<ProductResponse> result = List.of();

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/selling"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.message").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.data").isArray())
        ;
    }
}