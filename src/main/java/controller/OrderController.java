package controller;

import manager.OrderManager;
import model.Order;
import model.Product;
import repository.ObjectOrderRepository;
import repository.OrderRepository;
import repository.TxtOrderRepository;

import java.util.Map;
import java.util.Scanner;

public class OrderController {
    private OrderManager orderManager;
    private Scanner scanner;

    public OrderController() {
        this.orderManager = new OrderManager(new ObjectOrderRepository());
        this.scanner = new Scanner(System.in);
        orderManager.loadOrder();
    }

    // 주문 생성
    // 카트에서 목록 로딩 후 예외처리 추가
    public void createOrder() {
        orderManager.generateTestOrders(800);
        Map<Product, Integer> loadedCartItem = orderManager.loadCartItem();
        if (loadedCartItem == null) {
            displayMessage("장바구니가 비어있습니다. 주문을 생성할 수 없습니다.");
            return;
        }
        for (Map.Entry<Product, Integer> entry : loadedCartItem.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println("수량: " + entry.getValue());
        }
        displayMessage("위 내용대로 주문하시겠습니까?? (y/n)");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("y")) {
            orderManager.createOrder(loadedCartItem);
            orderManager.flushOrder();
            displayMessage("주문이 완료되었습니다.");
        } else {
            displayMessage("메뉴로 화면으로 돌아갑니다.");
        }

    }

    // 주문 삭제
    public void removeOrder(Integer orderId) {
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
    public void saveOrderWithTxt(){
        OrderRepository temp = orderManager.getRepository();
        orderManager.setRepository(new TxtOrderRepository());
        orderManager.flushOrder();
        orderManager.setRepository(temp);
    }
}
