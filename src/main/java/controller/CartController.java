package controller;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import lombok.Getter;
import manager.CartManager;
import manager.ProductManager;
import manager.ProductSelector;
import model.Cart;
import model.Product;

@Getter
public class CartController {
	
    private Scanner scanner;
	private CartManager cartManager;
	private ProductSelector productSelector;
	private ProductManager productManager;
	private Map<Product, Integer> cart; // 장바구니 (상품, 수량)
	
	public CartController(ProductManager productManager, ProductSelector productSelector, Scanner scanner) {
		this.scanner = scanner;
		this.cartManager = new CartManager(new Cart());
		this.productManager = productManager;
		this.productSelector = productSelector;
		this.cart = this.cartManager.getCart().getCartItems();
	}
	
	public void run() {
		
		while(true) {
			showCart();
			System.out.println("\t[1. 상품 추가하기]");
			System.out.println("\t[2. 상품 삭제하기]");
			System.out.println("\t[3. 상품 수량 변경하기]");
			System.out.println("\t[4. 이전 화면으로 돌아가기]");
			System.out.print("선택 : ");
			
			int choice = Integer.parseInt(scanner.nextLine());
			
			switch(choice) {
			case 1 :
				addProduct();
				break;
			case 2 :
				deleteProduct();
				break;
			case 3 :
				changeQuantity();
				break;
			case 4 :
				return;
			default :
				System.out.println("올바른 번호를 선택해주세요!");	
			}
		}
	}
	
	// 카트 출력
	public void showCart() {
		if (cart.isEmpty()) {
			System.out.println("============== 장바구니 목록 ==============");
            System.out.println("장바구니가 비어 있습니다. 상품을 추가해주세요!!");
            return;
        }

        System.out.println("============== 장바구니 목록 ==============");
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            
            // 커피인지 디저트(케이트)인지 구분한후 각 제품에 맞는 옵션 보여주기
            System.out.println("[상품번호 : " + product.getProductId() + "] " + product.showOption() + ", 수량: " + quantity);
        }
        showTotalPrice();
	}
	
	
	// 메뉴를 선택하고 카트에 상품 추가
	public void addProduct() {
		Product product = productSelector.selectProduct();
		
		if (product == null) {
            System.out.println("상품 선택이 올바르지 않습니다.");
            return;
        }
		this.cartManager.addProduct(product); // Manager에서 비즈니스 로직 처리
		

	}
	
	// 카트에 담긴 상품 삭제
	public void deleteProduct() {
		System.out.println("삭제할 상품의 상품번호를 입력해주세요:");
        int productId = Integer.parseInt(scanner.nextLine());
        
        List<Product> matchingProducts = cartManager.getMatchingProducts(productId);
        if (matchingProducts.isEmpty()) {
            System.out.println("장바구니에 해당 상품이 없습니다.");
            return;
        }
        
        // 사용자 입력을 통해서 상품의 옵션 정보를 받고 그 상품을 선택하게 이후 ... 삭제시킴
        Product productToRemove = selectProductFromList(matchingProducts);
        if (productToRemove != null) {
            cartManager.deleteProduct(productToRemove);
            System.out.println(productToRemove.getName() + "이(가) 장바구니에서 삭제되었습니다.");
        }
	}
	
	// 카트에 담긴 상품의 수량 조절
	public void changeQuantity() {
        System.out.println("수량을 변경할 상품의 상품번호를 입력해주세요:");
        int productId = Integer.parseInt(scanner.nextLine());

        List<Product> matchingProducts = cartManager.getMatchingProducts(productId);
        if (matchingProducts.isEmpty()) {
            System.out.println("장바구니에 해당 상품이 없습니다.");
            return;
        }

        Product productToChange = selectProductFromList(matchingProducts);
        if (productToChange == null) return;

        System.out.println("새로운 수량을 입력하세요:");
        int newQuantity = Integer.parseInt(scanner.nextLine());

        cartManager.updateQuantity(productToChange, newQuantity);
        System.out.println(productToChange.getName() + "의 수량이 " + newQuantity + "(으)로 변경되었습니다.");
    }
	
	
	// 사용자 입력을 통해서 상품의 옵션 정보를 받고 그 상품을 선택
	private Product selectProductFromList(List<Product> products) {
        if (products.size() == 1) {
            return products.get(0);
        }

        System.out.println("옵션을 선택하세요:");
        for (int i = 0; i < products.size(); i++) {
            System.out.println((i + 1) + ". " + products.get(i).showOption());
        }

        System.out.print("옵션 번호 입력: ");
        int optionIndex = Integer.parseInt(scanner.nextLine()) - 1;

        if (optionIndex < 0 || optionIndex >= products.size()) {
            System.out.println("잘못된 입력입니다.");
            return null;
        }

        return products.get(optionIndex);
    }
	
	// 카트에 담긴 상품들의 총 가격을 보여줌
	public void showTotalPrice() {
		// Manager에서 비즈니스 로직 처리
		System.out.printf("\n%40s [총 가격: %,d원]\n", "", cartManager.calTotalPrice());
	}
	
	// 카트 불러오기
	public Map<Product, Integer> getCartItems(){
		return cartManager.getCartItems();
	}
}
