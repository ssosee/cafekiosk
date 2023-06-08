package sample.cafekiosk.spring.api.service.order.requset;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
public class OrderCreateService {

    private List<String> productNumbers;

    @Builder
    private OrderCreateService(List<String> productNumbers) {
        this.productNumbers = productNumbers;
    }
}
