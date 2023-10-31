package ibm.elizapimentel.DummyProducts.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Products implements Serializable {

    private List<ProductsResponse> products;
}
