package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

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
        Product product1 = createProduct("001", ProductType.HANDMADE, ProductSellingType.SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", ProductType.HANDMADE, ProductSellingType.HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", ProductType.BAKERY, ProductSellingType.STOP_SELLING, "크루아상", 3500);

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
    @DisplayName("상품번호로 상품들을 조회한다.")
    void findAllByProductNumberIn() {
        // given
        Product product1 = createProduct("001", ProductType.HANDMADE, ProductSellingType.SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", ProductType.HANDMADE, ProductSellingType.HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", ProductType.BAKERY, ProductSellingType.STOP_SELLING, "크루아상", 3500);

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

    @Test
    @DisplayName("가장 마지막에 저장한 상품의 번호를 읽어 온다.")
    void findLatestProductNumber() {
        // given
        String targetProductNumber = "003";
        Product product1 = createProduct("001", ProductType.HANDMADE, ProductSellingType.SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", ProductType.HANDMADE, ProductSellingType.HOLD, "카페라떼", 4500);
        Product product3 = createProduct(targetProductNumber, ProductType.BAKERY, ProductSellingType.STOP_SELLING, "크루아상", 3500);

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        String latestProductNumber = productRepository.findLatestProductNumber();

        // then
        assertThat(latestProductNumber).isEqualTo(targetProductNumber);
    }

    @Test
    @DisplayName("가장 마지막에 저장한 상품의 번호를 읽어 온다.")
    void findLatestProductNumberWhenProductIsEmpty() {
        // when
        String latestProductNumber = productRepository.findLatestProductNumber();

        // then
        assertThat(latestProductNumber).isNull();
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