package sample.cafekiosk.spring.api.service.order;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
// @DataJpaTest
class OrderServiceTest {
    @Autowired
    OrderService orderService;
    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    void test() {
        // given
        Product product1 = createProduct("001", ProductType.HANDMADE, ProductSellingType.SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", ProductType.HANDMADE, ProductSellingType.HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", ProductType.BAKERY, ProductSellingType.STOP_SELLING, "크루아상", 3500);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "002"))
                .build();

        LocalDateTime now = LocalDateTime.now();

        // when
        OrderResponse orderResponse = orderService.createOrder(request, now);

        // then
        assertThat(orderResponse.getId()).isNotNull();

        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .containsExactlyInAnyOrder(now, 8500);

        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001", 4000),
                        Tuple.tuple("002", 4500)
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