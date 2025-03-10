package model;

public class CartMain {
	public static void main(String[] args) {
		Cart c = new Cart();
		c.addProduct();
		c.showCart();
		System.out.println();

		c.addProduct();
		c.showCart();
		System.out.println();

		c.addProduct();
		c.showCart();
		System.out.println();

		c.addProduct();
		c.showCart();
		System.out.println();

		c.addProduct();
		c.showCart();
		System.out.println();
		
		c.deleteProduct();
		c.showCart();
		System.out.println();
		
		c.changeQuantity();
		c.showCart();
		
	}
}
