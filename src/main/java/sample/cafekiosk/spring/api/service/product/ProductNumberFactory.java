package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sample.cafekiosk.spring.domain.product.ProductRepository;

@Component
@RequiredArgsConstructor
public class ProductNumberFactory {

    private final ProductRepository productRepository;

    public String createNextNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();

        if(latestProductNumber == null) return "001";

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        latestProductNumberInt += 1;

        // 9 -> 009, 10 -> 010
        return String.format("%03d", latestProductNumberInt);
    }
}
