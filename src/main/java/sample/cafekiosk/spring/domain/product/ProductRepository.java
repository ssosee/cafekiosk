package sample.cafekiosk.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * select *
     * form product
     * where product_selling_type in ('SELLING', 'HOLD');
     */
    List<Product> findAllByProductSellingTypeIn(List<ProductSellingType> productSellingTypes);
    List<Product> findAllByProductNumberIn(List<String> productsNumbers);
}
