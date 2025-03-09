package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dessert extends Product {
    private boolean isSlice; // 조각 여부

    public Dessert(int productId, String name, int price) {
        super(productId, name, price);
    }

    public String showOption() {
        return getName() + "(" + getPrice() + ") 옵션 - " +
            (isSlice ? "조각케이크" : "홀케이크");
    }
}
