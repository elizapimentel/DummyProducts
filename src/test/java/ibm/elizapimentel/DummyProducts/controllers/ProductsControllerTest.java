package ibm.elizapimentel.DummyProducts.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import ibm.elizapimentel.DummyProducts.mapper.ProductsMapper;
import ibm.elizapimentel.DummyProducts.model.ProductsRequest;
import ibm.elizapimentel.DummyProducts.model.dto.ProductsResponse;
import ibm.elizapimentel.DummyProducts.services.ProductsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static ibm.elizapimentel.DummyProducts.Common.Constants.CATEGORY;
import static ibm.elizapimentel.DummyProducts.Common.Constants.ID;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@WebMvcTest(controllers = ProductsController.class)
public class ProductsControllerTest {

    @MockBean
    private ProductsService service;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void mustReturnAllItemsFromClient() throws Exception {
        ProductsRequest request = new ProductsRequest();
        when(service.getAllProducts()).thenReturn(List.of(request));
        this.mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void mustReturnAllItemsFromDB() throws Exception {

        ProductsRequest prod = new ProductsRequest();
        prod.setTitle("iPhone 9");
        prod.setDescription("An apple mobile which is nothing like apple");
        prod.setPrice(549);
        prod.setDiscountPercentage(12.96);
        prod.setRating(4.69);
        prod.setStock(94);
        prod.setBrand("Apple");
        prod.setCategory("smartphones");
        prod.setThumbnail("https://i.dummyjson.com/data/products/1/thumbnail.jpg");
        prod.setImages(List.of("https://i.dummyjson.com/data/products/1/1.jpg","https://i.dummyjson.com/data/products/1/2.jpg","https://i.dummyjson.com/data/products/1/3.jpg","https://i.dummyjson.com/data/products/1/4.jpg","https://i.dummyjson.com/data/products/1/thumbnail.jpg"));

        ProductsResponse response = ProductsMapper.MAPPER.modelToDto(prod);

        when(service.getAllFromDB()).thenReturn(List.of(response));

        this.mockMvc.perform(get("/products/db")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void mustReturnProductById() throws Exception {
        ProductsResponse response = new ProductsResponse();
        when(service.getProdById(anyLong())).thenReturn(response);
        this.mockMvc.perform(get("/products/" + ID)
                        .content(asJsonString(response))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void mustReturnProductsByCategory() throws Exception {
        ProductsResponse response = new ProductsResponse();
        when(service.getByCategory(anyString())).thenReturn(List.of(response));
        this.mockMvc.perform(get("/products/category").param("category", CATEGORY)
                        .content(asJsonString(response))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
