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
        // 시안색상(Cyan)으로 통일
        String colorReset = "\u001B[0m";
        String colorCyan = "\u001B[36m";

        // 모든 텍스트를 시안색상으로 출력
        System.out.println(colorCyan + "██╗███╗   ██╗███████╗ ██████╗ ██████╗ ██████╗ ████████╗██████╗");
        System.out.println("██║████╗  ██║██╔════╝██╔══██╗██╔══██╗██╔══██╗██╔════╝██╔══██╗");
        System.out.println("██║██╔██╗ ██║███████╗██║  ██║██████╔╝██████╔╝█████╗  ██████╔╝");
        System.out.println("██║██║╚██╗██║╚════██║██║  ██║██╔══██╗██╔══██╗██╔══╝  ██╔══██╗");
        System.out.println("██║██║ ╚████║███████║╚██████╔╝██║  ██║██████╔╝███████╗██║  ██║");
        System.out.println("╚═╝╚═╝  ╚═══╝╚══════╝ ╚═════╝ ╚═╝  ╚═╝╚════╝ ╚══════╝╚═╝  ╚═╝");
        System.out.println(colorReset);
        while (true) {
            // 메뉴 제목 및 구분선
            System.out.println("\n=================== 카페 키오스크 ===================");
            System.out.println("📋 1. 메뉴 선택하기");
            System.out.println("🛒 2. 장바구니 보기");
            System.out.println("📝 3. 주문하기");
            System.out.println("📋 4. 주문 처리 현황 보기");
            System.out.println("🔑 5. 관리자 화면");
            System.out.println("❌ 6. 종료");
            System.out.print("👉 선택: ");

            // 사용자 입력 처리
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
                    orderController.getRecentOrder();
                    break;
                case 5:
                    adminController.viewAdminLoginMenu();
                    break;
                case 6:
                    scanner.close();
                    orderController.close();
                    return;
                default:
                    System.out.println("⚠️ 올바른 번호를 입력하세요.");
            }
        }
    }
}
