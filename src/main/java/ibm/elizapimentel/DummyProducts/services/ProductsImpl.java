package ibm.elizapimentel.DummyProducts.services;

import ibm.elizapimentel.DummyProducts.client.DummyClient;
import ibm.elizapimentel.DummyProducts.mapper.ProductsMapper;
import ibm.elizapimentel.DummyProducts.model.ProductsRequest;
import ibm.elizapimentel.DummyProducts.model.dto.ProductsResponse;
import ibm.elizapimentel.DummyProducts.repositories.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductsImpl implements ProductsService{
    @Autowired
    private DummyClient client;
    @Autowired
    private ProductsRepository repo;
    @Autowired
    private ProductsMapper mapper;


    @Override
    public List<ProductsRequest> getAllProducts() {
        List<ProductsResponse> response = client.getAllProducts().getProducts();
        List<ProductsRequest> requestList = response.stream()
                .map(ProductsMapper.MAPPER::dtoToModel)
                .collect(Collectors.toList());
        return repo.saveAll(requestList);
    }
}
