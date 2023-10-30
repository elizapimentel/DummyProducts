package ibm.elizapimentel.DummyProducts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductsResponse implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotNull
    private String title;
    private String description;
    private Integer price;
    private Double discountPercentage;
    private  Double rating;
    @NotNull
    private Integer stock;
    @NotNull
    private  String brand;
    @NotNull
    private  String category;
    private String thumbnail;
    private String images;
}
