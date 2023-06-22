package sample.cafekiosk.spring.api.service.product.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingType;
import sample.cafekiosk.spring.domain.product.ProductType;

import javax.persistence.*;

@Data
// @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductResponse {
    private Long id;
    private String productNumber;
    private ProductType productType;
    private ProductSellingType productSellingType;
    private String name;
    private int price;

    @Builder
    private ProductResponse(Long id, String productNumber, ProductType productType, ProductSellingType productSellingType, String name, int price) {
        this.id = id;
        this.productNumber = productNumber;
        this.productType = productType;
        this.productSellingType = productSellingType;
        this.name = name;
        this.price = price;
    }

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .productNumber(product.getProductNumber())
                .productType(product.getProductType())
                .productSellingType(product.getProductSellingType())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
