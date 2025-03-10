package controller;

import manager.ProductManager;
import model.Cart;

import java.util.Scanner;

public class MainController {
    private Scanner scanner;
    private Cart cart;
    private OrderController orderController;
    private ProductManager productManager;

    public MainController() {
        this.scanner = new Scanner(System.in);
        this.cart = new Cart();
        this.orderController = new OrderController();
        this.productManager = new ProductManager();
    }

    public void start() {
        while (true) {
            System.out.println("=== 카페 키오스크 ===");
            System.out.println("1. 메뉴 선택하기");
            System.out.println("2. 장바구니 보기");
            System.out.println("3. 주문하기");
            System.out.println("4. 관리자 화면");
            System.out.println("5. 종료");
            System.out.print("선택: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("상품 목록 출력");
                    System.out.println("이후 카트에 담는 로직 필요");
                    break;
                case 2:
                    cart.showCart();
                    break;
                case 3:
                    orderController.createOrder();
                    for (int i = 0; i < 800; i++) {
                        orderController.completeOrder(i);
                    }
                    orderController.saveOrderWithTxt();
                    break;
                case 4:
                    System.out.println("관리자 화면 출력");
                    break;
                case 5:
                    System.out.println("프로그램 종료");
                    return;
                default:
                    System.out.println("올바른 번호를 입력하세요.");
            }
        }
    }
}
