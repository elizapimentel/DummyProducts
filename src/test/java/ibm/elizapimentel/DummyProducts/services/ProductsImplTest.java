package ibm.elizapimentel.DummyProducts.services;

import ibm.elizapimentel.DummyProducts.client.DummyClient;
import ibm.elizapimentel.DummyProducts.mapper.ProductsMapper;
import ibm.elizapimentel.DummyProducts.model.ProductsRequest;
import ibm.elizapimentel.DummyProducts.model.dto.Products;
import ibm.elizapimentel.DummyProducts.model.dto.ProductsResponse;
import ibm.elizapimentel.DummyProducts.repositories.ProductsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ibm.elizapimentel.DummyProducts.Common.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductsImplTest {

    @InjectMocks
    private ProductsImpl service;
    @Mock
    private ProductsRepository repo;
    @Mock
    private DummyClient client;
    @Mock
    private ProductsMapper mapper;

    private ProductsRequest productsRequest;
    private ProductsResponse productsResponse;
    private Optional<ProductsRequest> optionalProductsReq;
    private Products dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        buildProd();
    }

    @Test
    void mustMapRequestToResponse() {
        ProductsRequest req = productsRequest;
        ProductsResponse res = mapper.modelToDto(req);

        assertThat(res).isNotNull();
        assertThat(res.getId()).isEqualTo(req.getId());
        assertThat(res.getTitle()).isEqualTo(req.getTitle());
        assertThat(res.getDescription()).isEqualTo(req.getDescription());
        assertThat(res.getBrand()).isEqualTo(req.getBrand());
        assertThat(res.getCategory()).isEqualTo(req.getCategory());
        assertThat(res.getRating()).isEqualTo(req.getRating());
        assertThat(res.getPrice()).isEqualTo(req.getPrice());
        assertThat(res.getDiscountPercentage()).isEqualTo(req.getDiscountPercentage());
        assertThat(res.getThumbnail()).isEqualTo(req.getThumbnail());
        assertThat(res.getImages()).isEqualTo(req.getImages());
        assertThat(res.getStock()).isEqualTo(req.getStock());
    }

    @Test
    void mustReturnAllDataFromClient() {
        when(client.getAllProducts()).
                thenReturn(dto);
        when(repo.saveAll(any()))
                .thenReturn(List.of(productsRequest));
        service.getAllProducts();
        verify(client, times(1))
                .getAllProducts();
        assertThat(mapper.dtoToModel(productsResponse)).isEqualTo(productsRequest);
    }

    @Test
    void mustReturnAllDataFromDB() {
        when(repo.findAll()).thenReturn(List.of(productsRequest));

        List<ProductsResponse> response = service.getAllFromDB();

        assertThat(productsRequest).isEqualTo(mapper.dtoToModel(productsResponse));
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(ProductsResponse.class, response.get(0).getClass());
        assertEquals(ID, response.get(0).getId());
        assertEquals(TITLE, response.get(0).getTitle());
        assertEquals(DESCRIPTION, response.get(0).getDescription());
        assertEquals(PRICE, response.get(0).getPrice());
        assertEquals(DISCOUNT_PERCENTAGE, response.get(0).getDiscountPercentage());
        assertEquals(RATING, response.get(0).getRating());
        assertEquals(STOCK, response.get(0).getStock());
        assertEquals(BRAND, response.get(0).getBrand());
        assertEquals(CATEGORY, response.get(0).getCategory());
        assertEquals(THUMBNAIL, response.get(0).getThumbnail());
        assertEquals(IMAGES, response.get(0).getImages());
    }

    @Test
    void mustReturnSearchById() {
        when(repo.findById(anyLong())).thenReturn(optionalProductsReq);

        ProductsResponse res = service.getProdById(ID);

        assertNotNull(res);
        assertEquals(ProductsResponse.class, res.getClass());
        assertEquals(ID, res.getId());
    }

    @Test
    void mustReturnSearchByIdNotFound() {
        assertThrows(Error.class,
                () -> service.getProdById(ID));
    }

    @Test
    void mustReturnSearchByCategory() {
        when(repo.searchCategory(anyString()))
                .thenReturn((List.of(productsRequest)));
        when(repo.findAll()).thenReturn(List.of(productsRequest));

        List<ProductsResponse> res = service.getByCategory(productsResponse.getCategory());

        verify(repo, times(1)).searchCategory(productsResponse.getCategory());
        assertNotNull(res);
        assertThat(productsRequest).isEqualTo(mapper.dtoToModel(productsResponse));
    }

    @Test
    void mustReturnNullCategory() {
        List<ProductsRequest> returnNull = new ArrayList<>();
        when(repo.findAll()).thenReturn(returnNull);

        List<ProductsResponse> response = service.getByCategory(null);

        assertThat(response.size()).isEqualTo(0);
        verify(repo).findAll();

    }

    @Test
    void mustReturnNullCategoryAndEmptyRepo() {
        when(repo.findAll()).thenReturn(Collections.emptyList());

        List<ProductsResponse> res = service.getByCategory(null);

        assertEquals(Collections.emptyList(), res);
        verify(repo).findAll();
    }

    @Test
    void mustCreateNewProduct() {
        when(repo.save(any())).thenReturn(productsRequest);

        ProductsResponse response = service.postNewProduct(productsResponse);

        assertNotNull(response);
        assertThat(productsRequest).isEqualTo(mapper.dtoToModel(productsResponse));
    }

    @Test
    void mustCreateNewProductNull() {
        ProductsRequest req = productsRequest;
        ProductsResponse res = mapper.modelToDto(req);

        when(repo.save(req)).thenReturn(req);

        try {
            service.postNewProduct(res);
        }catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
    }

    @Test
    void mustReturnProductUpdated() {
        when(repo.findById(anyLong())).thenReturn(optionalProductsReq);
        when(repo.save(any())).thenReturn(productsRequest);

        ProductsResponse res = service.updateProduct(productsResponse);

        assertNotNull(res);
        assertThat(productsRequest).isEqualTo(mapper.dtoToModel(productsResponse));
        assertEquals(ProductsResponse.class, res.getClass());
        assertEquals(ID, res.getId());
        assertEquals(TITLE, res.getTitle());
        assertEquals(DESCRIPTION, res.getDescription());
        assertEquals(PRICE,res.getPrice());
        assertEquals(DISCOUNT_PERCENTAGE, res.getDiscountPercentage());
        assertEquals(RATING, res.getRating());
        assertEquals(STOCK, res.getStock());
        assertEquals(BRAND, res.getBrand());
        assertEquals(CATEGORY, res.getCategory());
        assertEquals(THUMBNAIL, res.getThumbnail());
        assertEquals(IMAGES, res.getImages());
    }

    @Test
    void mustDeleteProductById() {
        when(repo.findById(anyLong())).thenReturn(optionalProductsReq);
        doNothing().when(repo).deleteById(anyLong());
        service.deleteProduct(ID);
        verify(repo, times(1)).deleteById(anyLong());
    }

    private void buildProd() {
        productsRequest = new ProductsRequest(ID, TITLE, DESCRIPTION, PRICE, DISCOUNT_PERCENTAGE, RATING,
                STOCK, BRAND, CATEGORY, THUMBNAIL, IMAGES);
        optionalProductsReq = Optional.of(new ProductsRequest(ID, TITLE, DESCRIPTION, PRICE, DISCOUNT_PERCENTAGE, RATING,
                STOCK, BRAND, CATEGORY, THUMBNAIL, IMAGES));
        productsResponse = new ProductsResponse(ID, TITLE, DESCRIPTION, PRICE, DISCOUNT_PERCENTAGE, RATING,
                STOCK, BRAND, CATEGORY, THUMBNAIL, IMAGES, TOTAL, SKIP, LIMIT);
        dto = new Products(List.of(productsResponse));
        mapper = ProductsMapper.MAPPER;

    }


}
