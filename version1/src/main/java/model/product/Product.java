package model.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.BaseModel;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product extends BaseModel {
    @JsonProperty("categoryId")
    private UUID categoryId;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("maxCount")
    private int maxCount;

    @JsonProperty("description")
    private String description;

    @JsonProperty("photoURL")
    private String photoURL;

    public Product(String name, UUID categoryId, BigDecimal price, int maxCount, String description, String photoURL) {
        super(name);
        this.categoryId = categoryId;
        this.price = price;
        this.maxCount = maxCount;
        this.description = description;
        this.photoURL = photoURL;
    }
}
