package sample.cafekiosk.spring.api.service.order;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
// @Transactional
// @DataJpaTest
class OrderServiceTest {
    @Autowired
    OrderService orderService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderProductRepository orderProductRepository;
    @Autowired
    StockRepository stockRepository;

    // 테스트가 끝날 때마다 수행
    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    void createOrderWithStock() {
        Product product1 = createProduct("001", ProductType.HANDMADE, ProductSellingType.SELLING, "아메리카노", 1000);
        Product product2 = createProduct("002", ProductType.BOTTLE, ProductSellingType.HOLD, "사과주스", 2000);
        Product product3 = createProduct("003", ProductType.BAKERY, ProductSellingType.STOP_SELLING, "크루아상", 3000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        Stock stock3 = Stock.create("003", 2);
        stockRepository.saveAll(List.of(stock1, stock2, stock3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001", "002", "003"))
                .build();

        LocalDateTime now = LocalDateTime.now();

        // when
        OrderResponse orderResponse = orderService.createOrder(request.toServiceRequest(), now);

        // then
        assertThat(orderResponse.getId()).isNotNull();

        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .containsExactlyInAnyOrder(now, 7000);

        assertThat(orderResponse.getProducts()).hasSize(4)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001", 1000),
                        Tuple.tuple("001", 1000),
                        Tuple.tuple("002", 2000),
                        Tuple.tuple("003", 3000)
                );

        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(3)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("001", 0),
                        tuple("002", 1),
                        tuple("003", 1)
                );
    }

    @Test
    @DisplayName("주문 생성시 재고가 부족한 경우 예외가 발생한다.")
    void createOrderWithStockWithNoStock() {
        Product product1 = createProduct("001", ProductType.HANDMADE, ProductSellingType.SELLING, "아메리카노", 1000);
        Product product2 = createProduct("002", ProductType.BOTTLE, ProductSellingType.HOLD, "사과주스", 2000);
        Product product3 = createProduct("003", ProductType.BAKERY, ProductSellingType.STOP_SELLING, "크루아상", 3000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 0);
        Stock stock2 = Stock.create("002", 0);
        Stock stock3 = Stock.create("003", 0);
        stockRepository.saveAll(List.of(stock1, stock2, stock3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001", "002", "003"))
                .build();

        LocalDateTime now = LocalDateTime.now();

        // when // then
        assertThatThrownBy(() -> orderService.createOrder(request.toServiceRequest(), now))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족한 상품이 존재합니다.");

    }

    @Test
    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    void createOrder() {
        // given
        Product product1 = createProduct("001", ProductType.HANDMADE, ProductSellingType.SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", ProductType.HANDMADE, ProductSellingType.HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", ProductType.BAKERY, ProductSellingType.STOP_SELLING, "크루아상", 3500);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        Stock stock3 = Stock.create("003", 2);
        stockRepository.saveAll(List.of(stock1, stock2, stock3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "002"))
                .build();

        LocalDateTime now = LocalDateTime.now();

        // when
        OrderResponse orderResponse = orderService.createOrder(request.toServiceRequest(), now);

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

    @Test
    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    void createOrderWithDuplicateProductNumbers() {
        // given
        Product product1 = createProduct("001", ProductType.HANDMADE, ProductSellingType.SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", ProductType.HANDMADE, ProductSellingType.HOLD, "카페라떼", 4500);
        productRepository.saveAll(List.of(product1, product2));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        Stock stock3 = Stock.create("003", 2);
        stockRepository.saveAll(List.of(stock1, stock2, stock3));

        // 중복되는 상품번호 주문
        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001"))
                .build();

        LocalDateTime now = LocalDateTime.now();

        // when
        OrderResponse orderResponse = orderService.createOrder(request.toServiceRequest(), now);

        // then
        assertThat(orderResponse.getId()).isNotNull();

        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .containsExactlyInAnyOrder(now, 8000);

        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("001", 4000),
                        Tuple.tuple("001", 4000)
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