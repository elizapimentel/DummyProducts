package ibm.elizapimentel.DummyProducts.services;

import ibm.elizapimentel.DummyProducts.client.DummyClient;
import ibm.elizapimentel.DummyProducts.mapper.ProductsMapper;
import ibm.elizapimentel.DummyProducts.model.dto.ProductsResponse;
//import ibm.elizapimentel.DummyProducts.repositories.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ProductsImpl implements ProductsService{
    @Autowired
    private DummyClient client;
//    @Autowired
//    private ProductsRepository repo;
//    @Autowired
//    private ProductsMapper mapper;


    @Override
    public List<ProductsResponse> getAllProducts() {

        return client.getAllProducts().getProducts();
    }
}
