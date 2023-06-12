package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.service.mail.MailService;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
// 긴 네트워크를 소비하는 곳에는 굳이 @Transactional은 걸지 않는 것이 좋다.
public class OrderStaticsService {

    private final OrderRepository orderRepository;
    private final MailService mailService;

    /**
     * orderDate 에 주문완료(결제완료)된 주문들을 가져와서
     * 총 매출 합계를 계산하고
     * email 로 이메일을 전송한다.
     */
    public boolean sendOrderStatisticsMail(LocalDate orderDate, String email) {
        List<Order> orders = orderRepository.findOrdersBy(
                orderDate.atStartOfDay(),
                orderDate.plusDays(1).atStartOfDay(),
                OrderStatus.PAYMENT_COMPLETED
        );

        int totalAmount = orders.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        boolean result = mailService.sendMail(
                "no-reply@cafekiosk.com",
                email,
                String.format("[매충통계] %s", orderDate),
                String.format("총 매출 합계는 %s원 입니다.", totalAmount));

        if(!result) {
            throw new IllegalArgumentException("매출 통계 메일 전송에 실패했습니다.");
        }

        return true;
    }
}
