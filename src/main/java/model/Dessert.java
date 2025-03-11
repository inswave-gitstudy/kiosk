package model;

import java.util.Objects;

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
    
    @Override
    public boolean equals(Object o) {
    	if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Dessert dessert = (Dessert) o;
        return this.isSlice == dessert.isSlice;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isSlice);
    }
}
