package controller;

import manager.OrderManager;
import model.Coffee;
import model.Order;
import model.Product;
import repository.TxtOrderRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import lombok.Getter;

@Getter
public class OrderController {
    private OrderManager orderManager;
    private Scanner scanner;

    public OrderController(Scanner scanner) {
        this.orderManager = new OrderManager();
        this.scanner = scanner;
        // orderManager.loadOrder();
        orderManager.generateTestOrders(500);
    }

    // 주문 생성
    // 카트에서 목록 로딩 후 예외처리 추가
    public void createOrder(/* Cart cart*/) {
        // Map<Product, Integer> loadCartItem = cart.getProducts();
        Map<Product, Integer> loadedCartItem = new HashMap<>();
        if (loadedCartItem == null) {
            displayMessage("장바구니가 비어있습니다. 주문을 생성할 수 없습니다.");
            return;
        }
        for (Map.Entry<Product, Integer> entry : loadedCartItem.entrySet()) {
            displayMessage(entry.getKey().toString());
            displayMessage("수량: " + entry.getValue());
        }
        displayMessage("위 내용대로 주문하시겠습니까?? (y/n)");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("y")) {
            orderManager.createOrder(loadedCartItem);
            displayMessage("주문이 완료되었습니다.");
        } else {
            displayMessage("메뉴로 화면으로 돌아갑니다.");
        }

    }

    // 주문 삭제
    public void removeOrder(int orderId) {
        Order removedOrder = orderManager.removeOrder(orderId);
        if (removedOrder != null) {
            displayMessage("주문번호 " + orderId + "이 삭제되었습니다.");
        } else {
            displayMessage("해당 주문을 찾을 수 없습니다.");
        }
    }

    // 주문 조회
    public void showOrderDetails(int orderId) {
        Order order = orderManager.getOrderById(orderId);
        if (order == null) {
            displayMessage("해당 주문을 찾을 수 없습니다.");
        } else {
            displayOrderDetails(order);
        }
    }

    public Map<Integer,Order> getAllOrder(){
        return orderManager.getAllOrder();
    }

    // 모든 주문 조회
    public void showAllOrders() {
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
    public void completeOrder(int orderId) {
        boolean success = orderManager.completeOrder(orderId);
        if (success) {
            displayMessage("주문번호: " + orderId + " 완료 처리되었습니다.");
        } else {
            displayMessage("해당 주문을 찾을 수 없습니다.");
        }
    }

    // 주문 취소
    public void cancelOrder(int orderId) {
        boolean success = orderManager.cancelOrder(orderId);
        if (success) {
            displayMessage("주문번호: " + orderId + " 취소 되었습니다.");
        } else {
            displayMessage("해당 주문을 찾을 수 없습니다.");
        }
    }

    // 준비중인 주문 정보 출력
    public void displayPrepareOrder() {
        Map<Integer, Order> prePareOrder = orderManager.getPrePareOrder();
        if (prePareOrder.isEmpty()) {
            displayMessage("대기 중인 주문 정보가 없습니다.");
        }
        for (Order value : prePareOrder.values()) {
            displayOrderDetails(value);
        }

    }

    // 주문 목록 파일 저장
    public void saveOrder() {
        orderManager.flushOrder();
    }

    // 주문 목록 파일 로딩
    public void loadOrder() {
        orderManager.loadOrder();
    }

    // 메시지 출력
    private void displayMessage(String message) {
        System.out.println(message);
    }

    // 주문 상세 정보 출력
    public void displayOrderDetails(Order order) {
        System.out.println(order);
    }

    // 주문 목록 텍스트 파일로 저장
    public void saveOrderWithTxt() {
        new TxtOrderRepository(null).saveOrder(orderManager.getCompletedOrders());
    }

    // 특정 날짜의 주문 목록 로딩 (텍스트 파일)
    public void loadOrderWithTxt() {
        displayMessage("조회할 날짜를 입력하세요(YYYY/MM/DD): ");
        System.out.print("입력: ");
        String day = scanner.nextLine();
        Map<Integer, Order> loadedOrders = new TxtOrderRepository(day).loadOrder();

        if (loadedOrders.isEmpty()) {
            displayMessage(day + "에 완료된 주문이 없습니다.");
        } else {
            loadedOrders.values().forEach(this::displayOrderDetails);
        }
    }

    // 영수증 출력
    public void showRecipe() {
        displayMessage("영수증을 출력할 주문번호를 입력하세요:");
        System.out.print("입력: ");
        int orderId = Integer.parseInt(scanner.nextLine());
        Order order = orderManager.getOrderById(orderId);

        if (order == null) {
            displayMessage("없는 주문번호 입니다");
            return;
        }

        printReceipt(order);
    }

    // 영수증 포맷팅
    private void printReceipt(Order order) {
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
        receipt.append(String.format("주문 상태: %s\n", order.getStatus().getStatus()));
        receipt.append("=================================\n");

        System.out.println(receipt);
    }

    // 프로그램 종료 시 백업 스케줄러 종료
    public void shutdownScheduler() {
        orderManager.shutdownScheduler();
        displayMessage("주문 자동 백업 스케줄러를 종료");
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }
}
