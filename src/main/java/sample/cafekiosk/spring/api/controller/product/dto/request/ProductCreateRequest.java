package sample.cafekiosk.spring.api.controller.product.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

@Data
@NoArgsConstructor
public class ProductCreateRequest {
    private ProductType productType;
    private ProductSellingType productSellingType;
    private String name;
    private int price;

    @Builder
    private ProductCreateRequest(ProductType productType, ProductSellingType productSellingType, String name, int price) {
        this.productType = productType;
        this.productSellingType = productSellingType;
        this.name = name;
        this.price = price;
    }

    public Product toEntity(String nextNumber) {
        return Product.builder()
                .productNumber(nextNumber)
                .productType(productType)
                .productSellingType(productSellingType)
                .name(name)
                .price(price)
                .build();
    }
}
