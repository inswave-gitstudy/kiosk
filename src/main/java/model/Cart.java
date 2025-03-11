package model;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

public class Cart {
    private Map<Product, Integer> cart = new HashMap<>();

    public void addProduct(Product product) {
        cart.put(product, cart.getOrDefault(product, 0) + 1);
    }

    public void removeProduct(Product product) {
        cart.remove(product);
    }

    public int getTotalPrice() {
        return cart.entrySet().stream()
                .mapToInt(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }

    public boolean isEmpty() {
        return cart.isEmpty();
    }

    public Map<Product, Integer> getCartItems() {
        return cart;
    }

    public void updateQuantity(Product product, int quantity) {
        if (quantity <= 0) {
            cart.remove(product);
        } else {
            cart.put(product, quantity);
        }
    }
    
    public int calTotalPrice() {
        int totalPrice = 0;
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            totalPrice += entry.getKey().getPrice() * entry.getValue();
        }
        return totalPrice;
    }
}


