package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Coffee extends Product {
    private boolean isDecaf;
    private String beanType;
    private boolean isIced;

    public Coffee(long productId, String name, int price) {
        super(productId, name, price);
    }

    public String showOption() {
        return getName() + "(" + getPrice() + ") 옵션 - " +
            (isDecaf ? "디카페인, " : "카페인, ") +
            "원두 : " + beanType +
            (isIced ? ", 아이스" : ", 핫");
    }
}
