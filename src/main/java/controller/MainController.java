package controller;

import manager.CartManager;
import manager.OrderManager;
import manager.ProductManager;
import manager.ProductSelector;
import model.Cart;
import model.Product;

import java.util.Scanner;

public class MainController {
    private Scanner scanner;
    private CartController cartController;
    private OrderController orderController;
    private ProductManager productManager;
    private ProductSelector productSelector;
    private AdminController adminController;
    private SalesDataController salesDataController;

    public MainController() {
        this.scanner = new Scanner(System.in);
        this.productManager = new ProductManager();
        this.productSelector = new ProductSelector(productManager);
        this.cartController = new CartController(productManager, productSelector, scanner);
        this.orderController = new OrderController(scanner);
        this.salesDataController = new SalesDataController(productManager, orderController);
        this.adminController = new AdminController(productManager, scanner, orderController, salesDataController);
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
                    cartController.addProduct();
                    break;
                case 2:
                    cartController.run();
                    break;
                case 3:
                     orderController.createOrder(cartController.getCartItems());
                    break;
                case 4:
                    adminController.viewAdminLoginMenu();
                    break;
                case 5:
                    System.out.println("프로그램 종료");
                    scanner.close();
                    orderController.close();
                    return;
                default:
                    System.out.println("올바른 번호를 입력하세요.");
            }
        }
    }
}
