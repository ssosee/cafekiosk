package sample.cafekiosk.spring.api.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendRepository;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class OrderStaticsServiceTest extends IntegrationTestSupport {

    @Autowired
    OrderStaticsService orderStaticsService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    MailSendRepository mailSendRepository;
    @Autowired
    OrderProductRepository orderProductRepository;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        mailSendRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    void sendOrderStatisticsMail() {

        // given
        Product product1 = createProduct("001", ProductType.HANDMADE, ProductSellingType.SELLING, "아메리카노", 1000);
        Product product2 = createProduct("001", ProductType.HANDMADE, ProductSellingType.SELLING, "카페라떼", 2000);
        Product product3 = createProduct("001", ProductType.HANDMADE, ProductSellingType.SELLING, "카푸치노", 3000);
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);

        LocalDateTime now = LocalDateTime.of(2023, 6, 10, 0, 0);
        Order order1 = createPaymentCompletedOrder(products, now);
        Order order2 = createPaymentCompletedOrder(products, LocalDateTime.of(2023, 6, 9, 23, 59, 59));
        Order order3 = createPaymentCompletedOrder(products, LocalDateTime.of(2023, 6, 10, 23, 59, 59));
        Order order4 = createPaymentCompletedOrder(products, LocalDateTime.of(2023, 6, 11, 0, 0, 0));

        // 테스트할 때마다 진짜로 계속 메일을 보낼 것인가?
        // 에바다. mock 으로 처리하자
        // 이것을 우리는 stubbing 이라고 부른다.
        Mockito.when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(true);

        // when
        boolean result = orderStaticsService.sendOrderStatisticsMail(LocalDate.of(2023, 6, 10), "example@example.com");
        List<MailSendHistory> mailSendHistories = mailSendRepository.findAll();
        // then
        assertThat(result).isTrue();
        assertThat(mailSendHistories).hasSize(1)
                .extracting("content")
                .contains("총 매출 합계는 12000원 입니다.");
    }

    private Order createPaymentCompletedOrder(List<Product> products, LocalDateTime now) {
        Order order = Order.builder()
                .products(products)
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .registeredDateTime(now)
                .build();
        return orderRepository.save(order);
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