package model;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Product {
    private long productId;
    private String name;
    private int price;

    public Product(long productId, String name, int price) {
        this.productId = productId;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return name + " : " + price + "원";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productId == product.productId;

    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
