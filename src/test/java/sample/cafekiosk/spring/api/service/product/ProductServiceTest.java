package sample.cafekiosk.spring.api.service.product;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceTest {

    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("가장 최근 상품번호에서 1 증가한 상품번호로 신규 상품을 등록한다.")
    void createProduct() {
        // given
        Product product1 = createProduct("001", ProductType.HANDMADE, ProductSellingType.SELLING, "아메리카노", 4000);
        productRepository.saveAll(List.of(product1));

        ProductCreateRequest request = ProductCreateRequest.builder()
                .productSellingType(ProductSellingType.SELLING)
                .productType(ProductType.HANDMADE)
                .name("카푸치노")
                .price(4000)
                .build();

        // when
        ProductResponse productResponse = productService.createProduct(request);

        // then
        assertThat(productResponse).extracting("productNumber", "productType", "productSellingType", "name", "price")
                .contains("002", ProductType.HANDMADE, ProductSellingType.SELLING, "카푸치노", 4000);

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1)
                .extracting("productNumber", "productType", "productSellingType", "name", "price")
                .contains(
                        tuple("001", ProductType.HANDMADE, ProductSellingType.SELLING, "아메리카노", 4000),
                        tuple("002", ProductType.HANDMADE, ProductSellingType.SELLING, "카푸치노", 4000)
                );
    }

    @Test
    @DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품번호는 001 이다.")
    void createProductWhenIsEmpty() {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .productSellingType(ProductSellingType.SELLING)
                .productType(ProductType.HANDMADE)
                .name("아메리카노")
                .price(4000)
                .build();

        // when
        ProductResponse productResponse = productService.createProduct(request);

        // then
        assertThat(productResponse).extracting("productNumber", "productType", "productSellingType", "name", "price")
                .containsExactlyInAnyOrder("001", ProductType.HANDMADE, ProductSellingType.SELLING, "아메리카노", 4000);
    }

    private Product createProduct(String productNumber, ProductType productType, ProductSellingType productSellingType, String name, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .productType(productType)
                .productSellingType(productSellingType)
                .name(name)
                .price(price)
                .build();
    }
}