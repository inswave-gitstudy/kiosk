package model;


import java.util.*;

import lombok.Getter;
import manager.ProductManager;
import manager.ProductSelector;


@Getter
public class Cart {
    private Scanner sc;
    private List<Product> products; // ProductManager에서 정한 메뉴판에 있는 상품들
    private Map<Product, Integer> cart; // 장바구니 (상품, 수량)
    private ProductManager pm;
    private ProductSelector ps;

    public Cart() {
        this.cart = new HashMap<>();
        this.pm = new ProductManager();
        this.products = this.pm.getAllProducts();
        this.ps = new ProductSelector(this.pm);
        this.sc = new Scanner(System.in);
    }

    // 장바구니 출력
    public void showCart() {
        if (cart.isEmpty()) {
            System.out.println("장바구니가 비어 있습니다.");
            return;
        }

        System.out.println("======= 장바구니 목록 =======");
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            
            // 커피인지 디저트(케이트)인지 구분한후 각 제품에 맞는 옵션 보여주기
            if (product instanceof Coffee) {
                Coffee coffee = (Coffee) product;
                System.out.println(coffee.getName() + " " + coffee.showOption() + ", 수량: " + quantity);
            } else {
            	Dessert dessert = (Dessert) product;
                System.out.println(dessert.getName() +  " " + dessert.showOption() + ", 수량: " + quantity);
            }
        }
        System.out.println("총 가격: " + calTotalPrice() + "원");
    }

    // 상품 추가 (같은 옵션이면 수량 증가, 옵션이 다르면 새로운 항목 추가)
    public void addProduct() {
        Product product = ps.selectProduct(); // 상품 선택 화면에서 고른 상품
        
        if (product == null) {
            System.out.println("상품 선택이 올바르지 않습니다.");
            return;
        }

        // equals()와 hashCode()를 활용하여 같은 옵션이면 수량 증가
        cart.put(product, cart.getOrDefault(product, 0) + 1);

        System.out.println(product.getName() + 
            (product instanceof Coffee ? " " + ((Coffee) product).showOption() : "") + 
            " 추가됨. 현재 수량: " + cart.get(product));
    }

    // 상품 삭제
    public void deleteProduct() {
        System.out.println("삭제할 상품의 상품번호를 입력해주세요:");
        long productId = Long.parseLong(sc.nextLine());

        Product productToRemove = matchingProductInCart(productId);
        
        if(cart.containsKey(productToRemove)) {
        	cart.remove(productToRemove);
        	System.out.println(productToRemove.getName() + "가 장바구니에서 삭제되었습니다.");
        }

        /* 같은 옵션의 상품이 있는 경우 수량 감소, 1개 남았으면 삭제
        if (cart.get(productToRemove) > 1) {
            cart.put(productToRemove, cart.get(productToRemove) - 1);
            System.out.println(productToRemove.getName() + "이(가) 한 개 삭제되었습니다. 현재 수량: " + cart.get(productToRemove));
        } else {
            cart.remove(productToRemove);
            System.out.println(productToRemove.getName() + "이(가) 장바구니에서 삭제되었습니다.");
        }
        */
    }

    // 총 가격 계산
    public int calTotalPrice() {
        int totalPrice = 0;
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            totalPrice += entry.getKey().getPrice() * entry.getValue();
        }
        return totalPrice;
    }

    // 장바구니가 비었는지 확인
    public boolean isEmpty() {
        return cart.isEmpty();
    }
    
    // productId를 기반으로 장바구니에서 상품을 찾되, 옵션까지 고려하여 선택 가능하도록 개선
    public Product matchingProductInCart(Long productId) {
        List<Product> matchedProducts = new ArrayList<>();

        // 같은 productId를 가진 상품들을 리스트에 추가
        for (Product p : cart.keySet()) {
            if (p.getProductId() == productId) {
                matchedProducts.add(p);
            }
        }

        if (matchedProducts.isEmpty()) {
            System.out.println("장바구니에 해당 상품이 없습니다.");
            return null;
        }

        // 같은 productId를 가진 상품이 하나만 있으면 바로 반환
        if (matchedProducts.size() == 1) {
            return matchedProducts.get(0);
        }

        // 같은 productId를 가진 상품이 여러 개라면 옵션을 선택하도록 유도
        System.out.println("삭제할 상품의 옵션을 선택하세요:");
        for (int i = 0; i < matchedProducts.size(); i++) {
            System.out.println((i + 1) + ". " + matchedProducts.get(i).showOption());
        }

        System.out.print("옵션 번호 입력: ");
        int optionIndex = Integer.parseInt(sc.nextLine()) - 1;

        if (optionIndex < 0 || optionIndex >= matchedProducts.size()) {
            System.out.println("잘못된 입력입니다.");
            return null;
        }

        return matchedProducts.get(optionIndex);
    }


    // 수량 변경 기능 (사용자가 특정 상품의 수량을 변경할 수 있도록)
    public void changeQuantity() {
        System.out.println("수량을 변경할 상품의 상품번호를 입력해주세요:");
        long productId = Long.parseLong(sc.nextLine());

        Product productToChange = matchingProductInCart(productId);

        System.out.println("새로운 수량을 입력하세요:");
        int newQuantity = Integer.parseInt(sc.nextLine());

        if (newQuantity <= 0) {
            cart.remove(productToChange);
            System.out.println(productToChange.getName() + "이(가) 장바구니에서 삭제되었습니다.");
        } else {
            cart.put(productToChange, newQuantity);
            System.out.println(productToChange.getName() + "의 수량이 " + newQuantity + "(으)로 변경되었습니다.");
        }
    }
}
