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
import java.util.Optional;
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

    @Override
    public List<ProductsResponse> getAllFromDB() {
        List<ProductsRequest> req = repo.findAll();
        return req.stream()
                .map(prod -> mapper.MAPPER.modelToDto(prod))
                .collect(Collectors.toList());
    }

    @Override
    public ProductsResponse getProdById(Long id) {
        Optional<ProductsRequest> prod = repo.findById(id);
        return mapper.MAPPER.modelToDto(prod.orElseThrow(
                () -> new Error("Id not found"))
        );
    }

    @Override
    public List<ProductsResponse> getByCategory(String category) {
        if (category != null) {
            List<ProductsRequest> reqCategory = repo.searchCategory(category);
            return reqCategory.stream().map(cat -> mapper
                    .MAPPER.modelToDto(cat)).collect(Collectors.toList());
        }
        List<ProductsRequest> cat = repo.findAll();
        List<ProductsResponse> res = cat.stream()
                .map(prodcat -> mapper.MAPPER.modelToDto(prodcat))
                .collect(Collectors.toList());
        return res;
    }

    @Override
    public ProductsResponse postNewProduct(ProductsResponse prod) {
        ProductsRequest newProd = mapper.dtoToModel(prod);
        ProductsRequest saveReq = repo.save(newProd);
        return mapper.MAPPER.modelToDto(saveReq);
    }
}
