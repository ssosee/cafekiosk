package sample.cafekiosk.spring.api.service.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
class ProductNumberFactoryTest extends IntegrationTestSupport {
    @Autowired
    ProductNumberFactory productNumberFactory;

    @Autowired
    ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("최근 상품 번호가 null이 아니면 최근 상품 번호 + 1한 값의 상품 번호를 생성한다.")
    void createProductNumber() {
        // given
        Product product = Product.builder()
                .productNumber("001")
                .build();
        productRepository.save(product);

        // when
        String nextNumber = productNumberFactory.createNextNumber();

        // then
        assertThat(nextNumber).isEqualTo("002");
    }

    @Test
    @DisplayName("최근 상품 번호가 null이면 001 값의 상품 번호를 생성한다.")
    void createProductNumberInit() {
        // given
        Product product = Product.builder()
                .build();
        productRepository.save(product);

        // when
        String nextNumber = productNumberFactory.createNextNumber();

        // then
        assertThat(nextNumber).isEqualTo("001");
    }

}