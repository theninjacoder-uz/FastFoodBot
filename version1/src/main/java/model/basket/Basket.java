package model.basket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import model.BaseModel;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(callSuper = true)
public class Basket extends BaseModel {
    @JsonProperty("userId")
    private String userId;

    @JsonProperty("productName")
    private String productName;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("maxQuantity")
    private final int maxQuantity;

    {
        maxQuantity = 20;
    }

    public Basket(String name, String userId, String productName, int quantity, BigDecimal price, int maxQuantity) {
        super(name);
        this.userId = userId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }
}
