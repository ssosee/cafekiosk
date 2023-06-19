package sample.cafekiosk.spring.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

//@DataJpaTest
//@ActiveProfiles("test")
class StockRepositoryTest extends IntegrationTestSupport {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    StockRepository stockRepository;

    @Test
    @DisplayName("상품번호 리스트로 재고를 조회한다.")
    void test() {
        // given
        Stock stock1 = Stock.create("001", 3);
        Stock stock2 = Stock.create("002", 2);
        Stock stock3 = Stock.create("003", 1);
        stockRepository.saveAll(List.of(stock1, stock2, stock3));

        // when
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(List.of("001", "002", "003"));

        // then
        assertThat(stocks).hasSize(3)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("001", 3),
                        tuple("002", 2),
                        tuple("003", 1)
                );
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