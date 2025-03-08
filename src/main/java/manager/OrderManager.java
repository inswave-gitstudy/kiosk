package manager;

import lombok.Getter;
import model.Order;
import model.OrderStatus;
import model.Product;
import repository.OrderRepository;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class OrderManager {
    private Map<Long, Order> orders; // 주문 목록 (주문번호 -> 주문)
    private long nextOrderId; // 주문번호 증가를 위한 변수
    private static final int SAVE_THRESHOLD = 1000; // 주문 저장 갯수 (100개 단위로 저장)
    private OrderRepository repository; // 파일 저장 담당

    public OrderManager(OrderRepository repository) {
        this.orders = new LinkedHashMap<>();
        this.nextOrderId = 1L; // 주문번호 1부터 시작
        this.repository = repository; // 저장 스토리지 설정
        // loadOrder();
    }

    // 주문 생성
    public Order createOrder(Map<Product, Integer> cartItems) {
        if (cartItems.isEmpty()) {
            System.out.println("장바구니가 비어있습니다. 주문을 생성할 수 없습니다.");
            return null;
        }
        // 현재 사이즈가 임계값이면 일단 저장
        if (orders.size() % SAVE_THRESHOLD == 0 && !orders.isEmpty()) {
            flushOrder(); // 현재까지의 주문 목록 저장
            clearOrder(); // 주문 목록 비우기
        }

        Order order = new Order(nextOrderId, cartItems); // 주문 생성
        orders.put(nextOrderId, order); // 주문 저장
        nextOrderId++; // 주문번호 증가
        // cartItems.clearCart(); // 주문 후 장바구니 초기화
        return order;
    }

    // 주문 삭제
    public Order removeOrder(Long orderId) {
        System.out.println("주문번호 : " + orderId + "가 삭제되었습니다");
        return orders.remove(orderId);
    }

    // 주문 목록 초기화
    private void clearOrder() {
        nextOrderId = 1;
        orders = new LinkedHashMap<>();
    }

    // 주문 저장
    public void flushOrder() {
        repository.saveOrder(orders);
    }

    // 주문 조회 (주문번호로 찾기)
    public Order getOrder(long orderId) {
        return orders.get(orderId);
    }

    // 주문 완료 처리
    public void completeOrder(long orderId) {
        updateOrderStatus(orderId, OrderStatus.DONE);
    }

    // 주문 취소 처리
    public void cancelOrder(long orderId) {
        updateOrderStatus(orderId, OrderStatus.CANCEL);
    }

    // 주문 처리 상태 변경
    public void updateOrderStatus(long orderId, OrderStatus status) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setStatus(status);
            System.out.println("주문번호 " + orderId + "의 상태가 " + status.getStatus() + "으로 변경되었습니다.");
        } else {
            System.out.println("해당 주문을 찾을 수 없습니다.");
        }
    }

    // 모든 주문 조회
    public void getAllOrder() {
        if (orders.isEmpty()) {
            System.out.println("현재 등록된 주문이 없습니다.");
            return;
        }
        for (Order order : orders.values()) {
            System.out.println(order);
        }
    }

    // 주문 목록 로드
    public void loadOrder() {
        Map<Long, Order> loadedOrders = repository.loadOrder();
        if (!loadedOrders.isEmpty()) {
            this.orders.putAll(loadedOrders);
            this.nextOrderId = Collections.max(orders.keySet()) + 1;
        }
    }

}
