package sample.cafekiosk.spring.api.controller.product.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.service.product.requset.ProductCreateService;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class ProductCreateRequest {
    @NotNull(message = "상품 타입은 필수 입니다.")
    private ProductType productType;
    @NotNull(message = "상품 판매 타입은 필수 입니다.")
    private ProductSellingType productSellingType;
    @NotBlank(message = "상품명은 필수 입니다.")
    // @NotNull // ["", " "] -> 필터링
    // @NotEmpty // [""] -> X
    // @Max(20) // 컨트롤러에서 검증하는 것이 맞는지?
    private String name;
    @Positive(message = "상품 가격은 0원 이상이어야 합니다.")
    private int price;

    @Builder
    private ProductCreateRequest(ProductType productType, ProductSellingType productSellingType, String name, int price) {
        this.productType = productType;
        this.productSellingType = productSellingType;
        this.name = name;
        this.price = price;
    }

    public ProductCreateService toProductCreateService() {
        return ProductCreateService.builder()
                .productType(productType)
                .productSellingType(productSellingType)
                .name(name)
                .price(price)
                .build();
    }
}
