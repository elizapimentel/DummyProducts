package ibm.elizapimentel.DummyProducts.controllers;

import ibm.elizapimentel.DummyProducts.model.ProductsRequest;
import ibm.elizapimentel.DummyProducts.model.dto.ProductsResponse;
import ibm.elizapimentel.DummyProducts.services.ProductsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/products")
public class ProductsController {

    @Qualifier("productsImpl")
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

    @PostMapping("/newProd")
    public ResponseEntity<?> postNewProduct(@RequestBody ProductsResponse prod) {
        try {
            return ResponseEntity.status(201).body(service.postNewProduct(prod));
        } catch (Error e) {
            return ResponseEntity.status(422).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id, @RequestBody ProductsResponse oldProd
    ) {
        try {
            ProductsResponse actualProd = service.getProdById(id);
            BeanUtils.copyProperties(oldProd, actualProd, "id");
            return ResponseEntity.status(200).body(service.updateProduct(actualProd));
        } catch (Error e) {
            return ResponseEntity.status(400).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProd(@PathVariable Long id) {
        service.deleteProduct(id);
        ResponseEntity.status(204);
    }
}
