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
        //verifica se os campos estão preenchidos
        if ((prod.getTitle() == null || prod.getTitle().trim().isEmpty()) ||
                (prod.getDescription() == null || prod.getDescription().trim().isEmpty()) ||
                (prod.getPrice() == null) ||
                (prod.getDiscountPercentage() == null) ||
                (prod.getRating() == null) ||
                (prod.getBrand() == null || prod.getBrand().trim().isEmpty()) ||
                (prod.getCategory() == null || prod.getCategory().trim().isEmpty()) ||
                (prod.getThumbnail() == null || prod.getThumbnail().trim().isEmpty())) {
            throw new IllegalArgumentException("Existe algum campo não preenchido.");
        }

        // Verifica se já existe um produto com o mesmo título no banco de dados
        Optional<ProductsRequest> existingProduct = repo.findByTitle(prod.getTitle().toLowerCase());

        if (existingProduct.isPresent()) {
            throw new RuntimeException("Já existe um produto com o mesmo título.");
        } else {
            // Se o produto não existe, cria um novo e salva no banco de dados
            ProductsRequest newProd = mapper.dtoToModel(prod);
            newProd.setStock(prod.getStock());
            ProductsRequest saveReq = repo.save(newProd);
            return mapper.modelToDto(saveReq);
        }

    }

    @Override
    public ProductsResponse updateProduct(Long id, ProductsResponse updatedProduct) {
        // Verifica se o produto com o ID fornecido existe no banco de dados
        ProductsRequest existingProduct = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));

        // Atualiza os dados do produto existente com os dados fornecidos
        existingProduct.setTitle(updatedProduct.getTitle());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setDiscountPercentage(updatedProduct.getDiscountPercentage());
        existingProduct.setRating(updatedProduct.getRating());
        existingProduct.setStock(updatedProduct.getStock());
        existingProduct.setBrand(updatedProduct.getBrand());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setThumbnail(updatedProduct.getThumbnail());
        existingProduct.setImages(updatedProduct.getImages());

        // Salva o produto atualizado
        ProductsRequest updatedProductEntity = repo.save(existingProduct);

        // Converte e retorna o produto atualizado como ProductsResponse
        return mapper.MAPPER.modelToDto(updatedProductEntity);
    }

    @Override
    public void deleteProduct(Long id, boolean deleteWholeItem, int quantityToRemove) {
        // Verifica se o produto existe
        ProductsRequest product = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado de id: " + id));

        // Obter a quantidade em estoque do produto
        int stock = product.getStock();

        if (deleteWholeItem) {
            // Excluir o produto inteiro
            repo.deleteById(id);
        } else {
            // Verificar se há pelo menos a quantidade especificada em estoque
            if (stock >= quantityToRemove) {
                // Diminuir o estoque do produto pela quantidade especificada
                product.setStock(stock - quantityToRemove);
                repo.save(product);

                repo.updateTotalStock(id, stock - quantityToRemove);
            } else {
                // Se não houver estoque disponível suficiente, lançar uma exceção ou tratar conforme necessário
                throw new RuntimeException("Valor em estoque insuficiente");
            }
        }
    }


}

