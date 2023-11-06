package ibm.elizapimentel.DummyProducts.services;


import ibm.elizapimentel.DummyProducts.model.ProductsRequest;
import ibm.elizapimentel.DummyProducts.model.dto.ProductsResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductsService {
    List<ProductsRequest> getAllProducts();
    List<ProductsResponse> getAllFromDB();
    ProductsResponse getProdById(Long id);
    List<ProductsResponse> getByCategory(String category);
    ProductsResponse postNewProduct( ProductsResponse prod);
    ProductsResponse updateProduct (ProductsResponse prod);
}
