package model;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Product implements Serializable {
    private long productId;
    private String name;
    private int price;

    public Product(long productId, String name, int price) {
        this.productId = productId;
        this.name = name;
        this.price = price;
    }

}
