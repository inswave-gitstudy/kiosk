package model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class Coffee extends Product implements Serializable {
    private boolean isDecaf;
    private String beanType;
    private boolean isIced;

    public Coffee(int productId, String name, int price) {
        super(productId, name, price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Coffee coffee = (Coffee) o;
        return isDecaf == coffee.isDecaf &&
            isIced == coffee.isIced &&
            beanType.equals(coffee.beanType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isDecaf, isIced, beanType);
    }

    public String showOption() {
        return getName() + "(" + getPrice() + ") 옵션 - " +
            (isDecaf ? "디카페인, " : "카페인, ") +
            "원두 : " + beanType +
            (isIced ? ", 아이스" : ", 핫");
    }
}