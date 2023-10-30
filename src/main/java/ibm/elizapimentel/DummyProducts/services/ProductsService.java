package ibm.elizapimentel.DummyProducts.services;


import ibm.elizapimentel.DummyProducts.model.dto.ProductsResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductsService {
    List<ProductsResponse> getAllProducts();
}
