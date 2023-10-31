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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductsImplTest {

    private Long ID                                   = 1L;
    public static final String TITLE                  = "iPhone 9";
    public static final String DESCRIPTION            = "An apple mobile which is nothing like apple";
    public static final Integer PRICE                 = 549;
    public static final Double DISCOUNT_PERCENTAGE    = 12.96;
    public static final Double RATING                 = 4.69;
    public static final Integer STOCK                 = 94;
    public static final String BRAND                  = "Apple";
    public static final String CATEGORY               = "smartphones";
    public static final String THUMBNAIL              = "https://i.dummyjson.com/data/products/1/thumbnail.jpg";
    public static final List<String> IMAGES           = List.of("https://i.dummyjson.com/data/products/1/1.jpg", "https://i.dummyjson.com/data/products/1/2.jpg", "https://i.dummyjson.com/data/products/1/3.jpg", "https://i.dummyjson.com/data/products/1/4.jpg", "https://i.dummyjson.com/data/products/1/thumbnail.jpg");
    public static final Integer TOTAL                 = 100;
    public static final Integer SKIP                  = 0;
    public static final Integer LIMIT                 = 30;

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
    private Optional<ProductsResponse> optionalProductsRes;
    private Products dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); //chamas os mocks
        buildProd(); //chama o metodo criado para gerar os dados
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

    private void buildProd() {
        productsRequest = new ProductsRequest(ID, TITLE, DESCRIPTION, PRICE, DISCOUNT_PERCENTAGE, RATING,
                STOCK, BRAND, CATEGORY, THUMBNAIL, IMAGES);
        optionalProductsReq = Optional.of(new ProductsRequest(ID, TITLE, DESCRIPTION, PRICE, DISCOUNT_PERCENTAGE, RATING,
                STOCK, BRAND, CATEGORY, THUMBNAIL, IMAGES));
        productsResponse = new ProductsResponse(ID, TITLE, DESCRIPTION, PRICE, DISCOUNT_PERCENTAGE, RATING,
                STOCK, BRAND, CATEGORY, THUMBNAIL, IMAGES, TOTAL, SKIP, LIMIT);
        optionalProductsRes = Optional.of(new ProductsResponse(ID,TITLE, DESCRIPTION, PRICE, DISCOUNT_PERCENTAGE, RATING,
                STOCK, BRAND, CATEGORY, THUMBNAIL, IMAGES, TOTAL, SKIP, LIMIT));
        dto = new Products(List.of(productsResponse));
        mapper = ProductsMapper.MAPPER;

    }


}
