package ibm.elizapimentel.DummyProducts.services;


import ibm.elizapimentel.DummyProducts.model.ProductsRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductsService {
    List<ProductsRequest> getAllProducts();
}
