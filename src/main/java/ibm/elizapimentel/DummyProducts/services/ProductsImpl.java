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
import java.util.NoSuchElementException;
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
        for (ProductsResponse externalProduct : response) {
            Optional<ProductsRequest> existingProduct = repo.findByTitle(externalProduct.getTitle());
            if (existingProduct.isPresent()) {
                // Se o produto já existir no banco de dados, atualize as informações
                ProductsRequest updatedProduct = existingProduct.get();
                mapper.updateModelFromDto(externalProduct, updatedProduct);
                repo.save(updatedProduct);
            } else {
                // Se o produto não existir, crie um novo registro
                ProductsRequest newProduct = mapper.dtoToModel(externalProduct);
                repo.save(newProduct);
            }
        }
        return repo.findAll();
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
        // Verifica se já existe um produto com o mesmo título no banco de dados
        Optional<ProductsRequest> existingProduct = repo.findByTitle(prod.getTitle().toLowerCase());

        if (existingProduct.isPresent()) {
            // Se o produto com o mesmo título já existe, adiciona mais um item ao estoque
            ProductsRequest existingProd = existingProduct.get();
            existingProd.setStock(existingProd.getStock() + 1); // Incrementa o estoque em 1
            ProductsRequest updatedProd = repo.save(existingProd);
            return mapper.modelToDto(updatedProd);
        } else {
            // Se o produto não existe, cria um novo e salva no banco de dados
            ProductsRequest newProd = mapper.dtoToModel(prod);
            newProd.setStock(1);
            ProductsRequest saveReq = repo.save(newProd);
            return mapper.modelToDto(saveReq);
        }
    }

    @Override
    public ProductsResponse updateProduct(ProductsResponse prod) {
        getProdById(prod.getId());
        ProductsRequest update = mapper.dtoToModel(prod);
        ProductsRequest saveUpdatedProd = repo.save(update);
        return mapper.MAPPER.modelToDto(saveUpdatedProd);
    }

    @Override
    public void deleteProduct(Long id) {
        getProdById(id);
        repo.deleteById(id);
    }

}

