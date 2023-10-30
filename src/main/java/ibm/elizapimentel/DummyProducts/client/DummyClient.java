package ibm.elizapimentel.DummyProducts.client;

import ibm.elizapimentel.DummyProducts.model.dto.Products;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "products", url = "${api.dummy.products.url}")
public interface DummyClient {

    @GetMapping()
    Products getAllProducts();

}
   /* @RequestMapping(method = RequestMethod.GET, value = "/products")
    List<ProductsDTO> getAllProducts();

    @RequestMapping(method = RequestMethod.POST, value = "/products/{productId}", consumes = "application/json")
    ProductsDTO update(@PathVariable("productId") Long productId, ProductsDTO store);*/
