package sample.cafekiosk.spring.api.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/new")
    public void createProduct(ProductCreateRequest  request) {
        productService.createProduct(request);
    }

    @GetMapping("/selling")
    public ResponseEntity<List<ProductResponse>> getSellingProducts() {
        return new ResponseEntity<>(productService.getSellingProducts(), HttpStatus.OK);
    }
}
