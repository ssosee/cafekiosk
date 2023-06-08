package sample.cafekiosk.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * select *
     * form product
     * where product_selling_type in ('SELLING', 'HOLD');
     */
    List<Product> findAllByProductSellingTypeIn(List<ProductSellingType> productSellingTypes);
    List<Product> findAllByProductNumberIn(List<String> productsNumbers);

    @Query(value = "select p.product_number from product p" +
            " order by p.id desc limit 1", nativeQuery = true)
    String findLatestProductNumber();
}
