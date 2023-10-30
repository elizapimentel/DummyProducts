package ibm.elizapimentel.DummyProducts.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class ProductsRequest implements Serializable {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonProperty("id")
    @Id
    private Long id;
    private String title;
    private String description;
    private Integer price;
    private Double discountPercentage;
    private  Double rating;
    private Integer stock;
    private  String brand;
    private  String category;
    private String thumbnail;
    private String images;
}
