package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("원하는 판매 상태를 가진 상품들을 조회한다.")
    void findAllByProductSellingTypeIn() {
        // given
        Product product1 = Product.builder()
                .productNumber("001")
                .productType(ProductType.HANDMADE)
                .productSellingType(ProductSellingType.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        Product product2 = Product.builder()
                .productNumber("002")
                .productType(ProductType.HANDMADE)
                .productSellingType(ProductSellingType.HOLD)
                .name("카페라떼")
                .price(4500)
                .build();

        Product product3 = Product.builder()
                .productNumber("003")
                .productType(ProductType.BAKERY)
                .productSellingType(ProductSellingType.STOP_SELLING)
                .name("크루아상")
                .price(3500)
                .build();

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllByProductSellingTypeIn(List.of(ProductSellingType.SELLING, ProductSellingType.HOLD));

        // then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "productSellingType")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", ProductSellingType.SELLING),
                        tuple("002", "카페라떼", ProductSellingType.HOLD)
                );
    }

    @Test
    @DisplayName("")
    void findAllByProductNumberIn() {
        // given
        Product product1 = Product.builder()
                .productNumber("001")
                .productType(ProductType.HANDMADE)
                .productSellingType(ProductSellingType.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        Product product2 = Product.builder()
                .productNumber("002")
                .productType(ProductType.HANDMADE)
                .productSellingType(ProductSellingType.HOLD)
                .name("카페라떼")
                .price(4500)
                .build();

        Product product3 = Product.builder()
                .productNumber("003")
                .productType(ProductType.BAKERY)
                .productSellingType(ProductSellingType.STOP_SELLING)
                .name("크루아상")
                .price(3500)
                .build();

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        // then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "productSellingType")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", ProductSellingType.SELLING),
                        tuple("002", "카페라떼", ProductSellingType.HOLD)
                );
    }
}