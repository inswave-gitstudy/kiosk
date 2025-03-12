package manager;

import java.util.*;
import java.util.stream.Collectors;

import lombok.Getter;
import model.Cart;
import model.Product;

@Getter
public class CartManager {
	
	Cart cart;
	
	public CartManager(Cart cart) {
		this.cart = cart;
	}
		
	public void addProduct(Product product){
		cart.addProduct(product);
	}
	
	public void deleteProduct(Product product) {
		cart.removeProduct(product);
	}
	
	public List<Product> getMatchingProducts(long productId) {
		return cart.getCartItems().keySet().stream()
                .filter(p -> p.getProductId() == productId)
                .collect(Collectors.toList());
        
    }
	
	public void updateQuantity(Product product, int quantity) {
		cart.updateQuantity(product, quantity);
	}
	
	public int calTotalPrice() {
		return cart.calTotalPrice();
	}
	
	public Map<Product, Integer> getCartItems(){
		return cart.getCartItems();
	}
	
	
	
	
}
