package sample.cafekiosk.spring.api.controller.order.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.service.order.requset.OrderCreateService;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
public class OrderCreateRequest {

    @NotEmpty(message = "상품번호는 필수 입니다.")
    private List<String> productNumbers;

    @Builder
    private OrderCreateRequest(List<String> productNumbers) {
        this.productNumbers = productNumbers;
    }

    public OrderCreateService toServiceRequest() {
        return OrderCreateService.builder()
                .productNumbers(productNumbers)
                .build();
    }
}
