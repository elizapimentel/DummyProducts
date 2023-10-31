package ibm.elizapimentel.DummyProducts.controllers;

import ibm.elizapimentel.DummyProducts.model.ProductsRequest;
import ibm.elizapimentel.DummyProducts.model.dto.ProductsResponse;
import ibm.elizapimentel.DummyProducts.services.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductsService service;

    @GetMapping()
    public ResponseEntity<List<ProductsRequest>> getAll() {
        List<ProductsRequest> products = service.getAllProducts();
        return ResponseEntity.status(200).body(products);
    }

    @GetMapping("/db")
    public ResponseEntity<List<ProductsResponse>> getAllFromDB() {
        List<ProductsResponse> prod = service.getAllFromDB();
        return ResponseEntity.status(200).body(prod);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductsResponse> getById(
            @PathVariable(value = "id") Long id) {
            ProductsResponse prod = service.getProdById(id);
            return ResponseEntity.status(200).body(prod);
        }

    @GetMapping("/category")
    public ResponseEntity<List<ProductsResponse>> searchCategory(
            @RequestParam("category") String category) {
        List<ProductsResponse> getCategory = service.getByCategory(category);
        return ResponseEntity.status(200).body(getCategory);
    }
}