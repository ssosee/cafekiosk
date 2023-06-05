package sample.cafekiosk.spring.domain.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;
import sample.cafekiosk.spring.domain.orderproduct.OrderProduct;
import sample.cafekiosk.spring.domain.product.Product;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private int totalPrice;
    private LocalDateTime registeredDateTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    public static Order create(List<Product> products, LocalDateTime registeredDateTime) {
        return new Order(products, registeredDateTime);
    }

    public Order(List<Product> products, LocalDateTime registeredDateTime) {
        this.orderStatus = OrderStatus.INIT;
        this.totalPrice = calculateTotalPrice(products);
        this.registeredDateTime = registeredDateTime;
        this.orderProducts = products.stream()
                .map(p -> new OrderProduct(this, p))
                .collect(Collectors.toList());
    }

    private static int calculateTotalPrice(List<Product> products) {
        return products.stream().mapToInt(Product::getPrice).sum();
    }

    public void changeOrderProduct(OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
        orderProduct.setOrder(this);
    }
}
