package sample.cafekiosk.spring.api.service.product.requset;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class ProductCreateService {
    private ProductType productType;
    private ProductSellingType productSellingType;
    private String name;
    private int price;

    @Builder
    private ProductCreateService(ProductType productType, ProductSellingType productSellingType, String name, int price) {
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
