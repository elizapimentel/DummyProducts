package ibm.elizapimentel.DummyProducts.controllers;

import ibm.elizapimentel.DummyProducts.model.ProductsRequest;
import ibm.elizapimentel.DummyProducts.model.dto.ProductsResponse;
import ibm.elizapimentel.DummyProducts.services.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @PathVariable Long id, @RequestBody ProductsResponse updatedProduct
    ) {
        try {
            // Atualiza o produto no serviço usando o objeto atualizado
            ProductsResponse updated = service.updateProduct(id, updatedProduct);

            // Retorna o produto atualizado como resposta
            return ResponseEntity.status(200).body(updated);
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProd(@PathVariable Long id,
                                        @RequestParam(required = false) Boolean deleteWholeItem,
                                        @RequestParam(required = false, defaultValue = "1") Integer quantityToRemove) {
        if (deleteWholeItem != null && deleteWholeItem) {
            // Excluir o item inteiro
            service.deleteProduct(id, true, 0);
        } else {
            // Diminuir apenas o estoque
            service.deleteProduct(id, false, quantityToRemove); // Passando a quantidade para remover
        }
        return ResponseEntity.status(204).build();
    }


}
