package model.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.BaseModel;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Product extends BaseModel {

    private UUID categoryId;
    private BigDecimal price;
    private int maxCount;
    private String description;
    private String photoURL;
}
