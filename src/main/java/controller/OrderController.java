package controller;

import lombok.Getter;
import manager.OrderManager;
import model.Coffee;
import model.Order;
import model.Product;

import java.util.Map;
import java.util.Scanner;

@Getter
public class OrderController {
    private OrderManager orderManager;
    private Scanner scanner;

    public OrderController(Scanner scanner) {
        this.orderManager = new OrderManager();
        this.scanner = scanner;
        orderManager.loadOrder();
        orderManager.startBackupScheduler(); // 2시간마다 자동 백업 실행
        orderManager.generateTestOrders(100);
        for (int i = 0; i < 100; i++) {
            orderManager.completeOrder(i);
        }
    }

    // 주문 생성, 카트에서 목록 로딩 후 예외처리 추가
    // MainController
    public void createOrder(Map<Product, Integer> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            displayMessage("장바구니가 비어있습니다. 주문을 생성할 수 없습니다.");
            return;
        }
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            displayMessage(entry.getKey().toString());
            displayMessage("수량: " + entry.getValue());
        }
        displayMessage("위 내용대로 주문하시겠습니까?? (1.이대로 주문 / 2. )");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("y")) {
            orderManager.createOrder(cartItems);
            displayMessage("주문이 완료되었습니다.");
            printReceipt();
        } else {
            displayMessage("메뉴로 화면으로 돌아갑니다.");
        }
    }

    // 모든 주문 조회
    // AdminController
    public void getAllOrder() {
        Map<Integer, Order> orders = orderManager.getAllOrder();
        if (orders.isEmpty()) {
            displayMessage("현재 등록된 주문이 없습니다.");
            return;
        }
        for (Order order : orders.values()) {
            displayOrderDetails(order);
        }
    }

    // 주문 완료
    // AdminController
    public void completeOrder() {
        displayMessage("완료할 주문 ID 를 입력하세요: ");
        int orderId = Integer.parseInt(scanner.nextLine());
        boolean success = orderManager.completeOrder(orderId);
        if (success) {
            displayMessage("주문번호: " + orderId + " 완료 처리되었습니다.");
        } else {
            displayMessage("존재하지 않는 주문번호 입니다.");
        }
    }

    // 주문 취소
    // AdminController
    public void cancelOrder() {
        displayMessage("취소할 주문 ID 를 입력하세요: ");
        int orderId = Integer.parseInt(scanner.nextLine());
        Order order = orderManager.removeOrder(orderId);
        if (order != null) {
            displayMessage("주문번호: " + orderId + " 취소 되었습니다.");
        } else {
            displayMessage("해당 주문을 찾을 수 없습니다.");
        }
    }

    // 준비중인 주문 정보 출력
    // AdminController
    public void getPrepareOrder() {
        Map<Integer, Order> prePareOrder = orderManager.getPrepareOrder();
        if (prePareOrder.isEmpty()) {
            displayMessage("대기 중인 주문 정보가 없습니다.");
        }
        for (Order value : prePareOrder.values()) {
            displayOrderDetails(value);
        }
    }

    // 메시지 출력
    private void displayMessage(String message) {
        System.out.println(message);
    }

    // 주문 상세 정보 출력
    private void displayOrderDetails(Order order) {
        System.out.println(order);
    }

    // 주문 목록 텍스트 파일 로딩 (완료처리된 주문 목록)
    // 통계처리를 위해 사용 (adminController)
    public Map<Integer, Order> loadOrderWithTxt() {
        return orderManager.loadOrderWithTxt();
    }

    // 영수증 콘솔 출력
    public void printReceipt() {
        Order lastOrder = orderManager.getLastOrder();
        displayMessage(formatReceipt(lastOrder));
        // TODO 영수증 저장할건지 물어보고 저장하는 기능 추가
        displayMessage("영수증을 파일로 저장할까요? 1.저장 2. 필요없음");
        int result = Integer.parseInt(scanner.nextLine());
        if(result == 1) saveReceipt();
    }

    // 영수증 파일 저장
    private void saveReceipt() {
        orderManager.saveReceipt();
    }

    // 영수증 포맷팅
    private String formatReceipt(Order order) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("\n========== [ 영수증 ] ==========\n");
        receipt.append(String.format("주문번호: %-10d | 주문일시: %s\n", order.getOrderId(), order.getDateTime()));
        receipt.append("---------------------------------\n");
        receipt.append(String.format("%-15s %-5s %10s\n", "상품명", "수량", "가격"));
        receipt.append("---------------------------------\n");

        for (Map.Entry<Product, Integer> entry : order.getProducts().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            int totalPrice = product.getPrice() * quantity;

            // 기본 상품 출력
            receipt.append(String.format("%-15s %-5d %,10d원\n", product.getName(), quantity, totalPrice));

            // 커피 옵션 추가
            if (product instanceof Coffee) {
                Coffee coffee = (Coffee) product;
                receipt.append(String.format("   [%s | %s | %s]\n",
                    coffee.isDecaf() ? "디카페인" : "카페인",
                    coffee.isIced() ? "아이스" : "핫",
                    coffee.getBeanType()));
            }
        }

        receipt.append("---------------------------------\n");
        receipt.append(String.format("총 수량: %-10d | 총 가격: %,10d원\n", order.getTotalAmount(), order.getTotalPrice()));
        receipt.append("=================================\n");

        return receipt.toString();
    }

    // 프로그램 종료 시 백업 스케줄러 종료
    private void shutdownScheduler() {
        orderManager.shutdownScheduler();
        displayMessage("주문 자동 백업 스케줄러를 종료");
    }

    // 종료 처리
    public void close() {
        orderManager.flushOrder();
        shutdownScheduler();
    }

}