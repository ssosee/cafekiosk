package sample.cafekiosk.spring.api.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.response.ApiResponse;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;

import javax.validation.Valid;
import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/new")
    public ApiResponse<?> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        return ApiResponse.of(HttpStatus.OK, productService.createProduct(request.toProductCreateService()));
    }

    @GetMapping("/selling")
    public ApiResponse<List<ProductResponse>> getSellingProducts() {
        return ApiResponse.of(HttpStatus.OK, productService.getSellingProducts());
    }
}
