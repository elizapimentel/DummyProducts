package ibm.elizapimentel.DummyProducts.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Products implements Serializable {

    @JsonProperty("products")
    private List<ProductsResponse> products;
}
