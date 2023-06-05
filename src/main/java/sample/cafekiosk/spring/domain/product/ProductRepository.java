package sample.cafekiosk.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * select *
     * form product
     * where product_selling_type in ('SELLING', 'HOLD');
     */
    List<Product> findAllByProductSellingTypeIn(List<ProductSellingType> productSellingTypes);
    List<Product> findAllByProductNumberIn(List<String> productsNumbers);
}
