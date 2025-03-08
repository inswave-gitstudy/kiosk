package model;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class Order implements Serializable {
    private static final long serialVersionUID = 1L; // 버전 관리
    private long orderId; // 주문번호 1~ 순차적 증가
    private Map<Product, Integer> products; // 주문된 상품 및 수량
    private int totalAmount; // 총 상품 개수
    private int totalPrice; // 총 가격
    private OrderStatus status; // 주문 처리 상태 PREPARE, IN PROCESS, CANCEL
    private LocalDateTime dateTime; // 주문 날짜 및 시간

    // 주문 생성자
    // 카트에서 정보 받아올때 사용
    public Order(long orderId, Map<Product, Integer> orderItems) {
        this(orderId, orderItems, OrderStatus.PREPARE, LocalDateTime.now());
    }

    // 주문 정보 I/O 작업시 사용
    public Order(long orderId, Map<Product, Integer> orderItems, OrderStatus status, LocalDateTime dateTime) {
        this.orderId = orderId;
        this.products = orderItems;
        this.totalAmount = calculateTotalAmount();
        this.totalPrice = calculateTotalPrice();
        this.status = status;
        this.dateTime = dateTime; // 현재 날짜 및 시간 저장
    }

    // 총 상품 개수 계산
    private int calculateTotalAmount() {
        int total = 0;
        for (Integer amount : products.values()) {
            total += amount;
        }
        return total;
    }

    // 총 가격 계산
    private int calculateTotalPrice() {
        int total = 0;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

    // 주문 완료 상태 변경
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    // 주문 정보 조회용 메서드
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("주문번호: " + orderId + "\n상품 목록:\n");
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            result.append("- ").append(entry.getKey().getName()).append(" x ").append(entry.getValue()).append(" (").append(entry.getKey().getPrice()).append("원)\n");
        }
        result.append("총 수량: ").append(totalAmount).append("\n");
        result.append("총 가격: ").append(totalPrice).append("원\n");
        result.append("주문 날짜: ").append(dateTime).append("\n");
        result.append("주문 상태: ").append(status.getStatus()).append("\n");
        return result.toString();
    }

}
