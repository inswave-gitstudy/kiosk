package model;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Product implements Serializable {
    private int productId;
    private String name;
    private int price;

    public Product(int productId, String name, int price) {
        this.productId = productId;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "상품ID: " + productId +
            ", 이름: " + name +
            ", 가격: " + price;
    }
}
