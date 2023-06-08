package sample.cafekiosk.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.requset.ProductCreateService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Transactional(readOnly = true)
 * readOnly = true : 읽기전용
 * CUD 동작 안함 X only Read
 * JPA : 스냅샷 저장, 변경 감지 안해도됨.(성능향상)
 *
 * CQRS : Command / Read 분리
 * 서비스에 따라 다르겠지만, 일반적으로 Read 작업이 많음
 *
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    // 상품 조회
    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllByProductSellingTypeIn(ProductSellingType.forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    // 상품 생성
    // 동시성 이슈...
    // UUID를 사용하면 동시성 고려 안해도된다...!
    @Transactional
    public ProductResponse createProduct(ProductCreateService request) {
        // productNumber 생성
        // 001, 002, 003, 004 ...
        // DB에서 마지막 저장된 Product의 상품 번호를 읽어와서 +1

        String nextNumber = createNextNumber();

        Product product = request.toEntity(nextNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    private String createNextNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();

        if(latestProductNumber == null) return "001";

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        latestProductNumberInt += 1;

        // 9 -> 009, 10 -> 010
        return String.format("%03d", latestProductNumberInt);
    }
}
