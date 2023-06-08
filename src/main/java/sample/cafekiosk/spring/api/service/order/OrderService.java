package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.requset.OrderCreateService;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    /**
     * 재고 감소 -> 동시성 고민
     * optimistic lock / pessimistic lock / ...
     */
    public OrderResponse createOrder(OrderCreateService request, LocalDateTime registeredDateTime) {

        List<String> productNumbers = request.getProductNumbers();
        List<Product> duplicateProducts = findProductsBy(productNumbers);

        deductStockQuantities(productNumbers, duplicateProducts);

        Order order = Order.create(duplicateProducts, registeredDateTime);

        Order savedOrder = orderRepository.save(order);

        return OrderResponse.of(savedOrder);
    }

    private void deductStockQuantities(List<String> productNumbers, List<Product> duplicateProducts) {
        // 재고차감 체크가 필요한 상품들 filter
        List<String> stockProductNumbers = extractStockProductNumbers(duplicateProducts);

        // 재고 엔티티 조회
        Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);

        // 상품별 counting
        Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);

        // 재고 차감 시도
        for(String productNumber : new HashSet<>(productNumbers)) {
            Stock stock = stockMap.get(productNumber);
            int quantity = productCountingMap.get(productNumber).intValue();

            // 재고가 부족한 경우
            if(stock.isQuantityLessThan(quantity)) {
                // deductQuantity()에서 체크하는데 서비스에서 한 번 더 체크하는 이유
                // 서비스 예외 메시지를 핸들링 하고 싶음
                throw new IllegalArgumentException("재고가 부족한 상품이 존재합니다.");
            }

            // 재고 차감
            stock.deductQuantity(quantity);
        }
    }

    private List<Product> findProductsBy(List<String> productNumbers) {
        // productNumbers 로 product 조회
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

        // map 으로 변환
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));

        // map을 product로 변환
        return productNumbers.stream()
                .map(productMap::get)
                .collect(Collectors.toList());
    }

    private static List<String> extractStockProductNumbers(List<Product> duplicateProducts) {
        List<String> stockProductNumbers = duplicateProducts.stream()
                .filter(product -> ProductType.containsStockType(ProductType.BAKERY))
                .map(Product::getProductNumber)
                .collect(Collectors.toList());
        return stockProductNumbers;
    }

    private Map<String, Stock> createStockMapBy(List<String> stockProductNumbers) {
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
        Map<String, Stock> stockMap = stocks.stream()
                .collect(Collectors.toMap(Stock::getProductNumber, p -> p));
        return stockMap;
    }

    private static Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
        Map<String, Long> productCountingMap = stockProductNumbers.stream()
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));
        return productCountingMap;
    }
}
