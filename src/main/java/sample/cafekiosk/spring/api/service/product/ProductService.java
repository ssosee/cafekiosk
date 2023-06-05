package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingType;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    // 상품 조회
    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllByProductSellingTypeIn(ProductSellingType.forDisplay());

        return products.stream()
                .map(p -> ProductResponse.of(p))
                .collect(Collectors.toList());
    }

    // 주문
}
