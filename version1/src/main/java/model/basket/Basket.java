package model.basket;

import java.math.BigDecimal;

public class Basket {
    private String userId;
    private String productName;
    private int quantity;
    private BigDecimal price;
    private final int maxQuantity;

    {
        maxQuantity = 20;
    }
}
