package com.poompich.training.backend.api;

import com.poompich.training.backend.business.ProductBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductApi {
    private final ProductBusiness productBusinessService;

    public ProductApi(ProductBusiness productBusinessService) {
        this.productBusinessService = productBusinessService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable("id") String id) {
        String response = productBusinessService.getProductById(id);
        return ResponseEntity.ok(response);
    }
}
