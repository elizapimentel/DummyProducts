package ibm.elizapimentel.DummyProducts.client;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.mock.HttpMethod;
import feign.mock.MockClient;
import ibm.elizapimentel.DummyProducts.model.dto.ProductsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.openfeign.support.SpringMvcContract;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DummyClientTest {
    private static String BASE_URL = "https://dummyjson.com/products";
    private static String RESPONSE = "{\"products\":[{\n" +
            "\t\t\"title\": \"iPhone 9\",\n" +
            "\t\t\"description\": \"An apple mobile which is nothing like apple\",\n" +
            "\t\t\"price\": 549,\n" +
            "\t\t\"discountPercentage\": 12.96,\n" +
            "\t\t\"rating\": 4.69,\n" +
            "\t\t\"stock\": 94,\n" +
            "\t\t\"brand\": \"Apple\",\n" +
            "\t\t\"category\": \"smartphones\",\n" +
            "\t\t\"thumbnail\": \"https://i.dummyjson.com/data/products/1/thumbnail.jpg\",\n" +
            "\t\t\"images\": [\n" +
            "\t\t\t\"https://i.dummyjson.com/data/products/1/1.jpg\",\n" +
            "\t\t\t\"https://i.dummyjson.com/data/products/1/2.jpg\",\n" +
            "\t\t\t\"https://i.dummyjson.com/data/products/1/3.jpg\",\n" +
            "\t\t\t\"https://i.dummyjson.com/data/products/1/4.jpg\",\n" +
            "\t\t\t\"https://i.dummyjson.com/data/products/1/thumbnail.jpg\"\n" +
            "\t\t]}]}";
    private DummyClient client;

    @Test
    public void whenGetAllProductsThenReturnOk(){
        this.builderFeignClient(new MockClient().ok(
                HttpMethod.GET,
                BASE_URL,
                RESPONSE
        ));

        List<ProductsResponse> listClient = client.getAllProducts().getProducts();

        assertThat(listClient).isNotEmpty();
        assertThat(listClient).hasSize(1);
    }

    private void builderFeignClient(MockClient mockClient){
        client = Feign.builder()
                .client(mockClient)
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .contract(new SpringMvcContract())
                .target(DummyClient.class, BASE_URL);
    }
}
