package model;


import java.util.*;

import lombok.Getter;
import manager.ProductManager;
import manager.ProductSelector;


@Getter
public class Cart {
    private Scanner sc;
    private List<Product> products; // ProductManager에서 정한 메뉴판에 있는 상품들
    private Map<Product, Integer> cart; // 장바구니 (상품 -> 수량)
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

            if (product instanceof Coffee) {
                Coffee coffee = (Coffee) product;
                System.out.println(coffee.getName() + " " + coffee.showOption() + ", 수량: " + quantity);
            } else {
                System.out.println(product + ", 수량: " + quantity);
            }
        }
        System.out.println("총 가격: " + calTotalPrice() + "원");
    }

    // 메뉴 출력
    public void showMenu() {
        System.out.println(products);
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

        Product productToRemove = null;

        for (Product p : cart.keySet()) {
            if (p.getProductId() == productId) {
                productToRemove = p;
                break;
            }
        }

        if (productToRemove == null) {
            System.out.println("장바구니에 해당 상품이 없습니다.");
            return;
        }
        else {
        	cart.remove(productToRemove);
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

    // 수량 변경 기능 (사용자가 특정 상품의 수량을 변경할 수 있도록)
    public void changeQuantity() {
        System.out.println("수량을 변경할 상품의 상품번호를 입력해주세요:");
        long productId = Long.parseLong(sc.nextLine());

        Product productToChange = null;

        for (Product p : cart.keySet()) {
            if (p.getProductId() == productId) {
                productToChange = p;
                break;
            }
        }

        if (productToChange == null) {
            System.out.println("장바구니에 해당 상품이 없습니다.");
            return;
        }

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
