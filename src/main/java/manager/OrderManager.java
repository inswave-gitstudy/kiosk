package manager;

import lombok.Getter;
import model.Coffee;
import model.Order;
import model.OrderStatus;
import model.Product;
import repository.OrderRepository;

import java.util.*;
import java.util.stream.IntStream;

@Getter
public class OrderManager {
    private Map<Integer, Order> orders; // 주문 목록 (주문번호 -> 주문)
    private int nextOrderId; // 주문번호 증가를 위한 변수
    private static final int SAVE_THRESHOLD = 1000; // 주문 저장 갯수 (100개 단위로 저장)
    private OrderRepository repository; // 파일 저장 담당

    public OrderManager(OrderRepository repository) {
        this.orders = new LinkedHashMap<>();
        this.nextOrderId = 1; // 주문번호 1부터 시작
        this.repository = repository; // 저장 스토리지 설정
    }

    public void setRepository(OrderRepository repository) {
        this.repository = repository;
    }


    // 주문 생성
    public void createOrder(Map<Product, Integer> cartItems) {
        // 현재 사이즈가 임계값이면 일단 저장
        if (orders.size() % SAVE_THRESHOLD == 0 && !orders.isEmpty()) {
            flushOrder(); // 현재까지의 주문 목록 저장
            clearOrder(); // 주문 목록 비우기
        }

        Order order = new Order(nextOrderId, cartItems); // 주문 생성
        orders.put(nextOrderId, order); // 주문 저장
        nextOrderId++; // 주문번호 증가
    }

    // 주문 삭제
    public Order removeOrder(Integer orderId) {
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
    public Order getOrderById(int orderId) {
        return orders.get(orderId);
    }

    // 주문 완료 처리
    public boolean completeOrder(int orderId) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setStatus(OrderStatus.DONE);
            return true;
        } else return false;
    }

    // 주문 취소 처리
    public boolean cancelOrder(int orderId) {
        Order order = orders.get(orderId);
        if (order != null) {
            removeOrder(orderId);
            return true;
        } else return false;
    }

    // 모든 주문 조회
    public Map<Integer, Order> getAllOrder() {
        return orders;
    }

    // 주문 목록 로드
    public void loadOrder() {
        Map<Integer, Order> loadedOrders = repository.loadOrder();
        if (!loadedOrders.isEmpty()) {
            this.orders.putAll(loadedOrders);
            this.nextOrderId = Collections.max(orders.keySet()) + 1;
        }
    }

    // 대기중인 주문만 조회
    public Map<Integer, Order> getPrePareOrder() {
        Map<Integer, Order> prepareOrders = new HashMap<>();
        for (Map.Entry<Integer, Order> entry : orders.entrySet()) {
            if (!entry.getValue().getStatus().equals(OrderStatus.DONE)) {
                prepareOrders.put(entry.getKey(), entry.getValue());
            }
        }
        return prepareOrders;
    }

    // 카트에서 상품 불러오기
    // 테스트 상품 추가
    public Map<Product, Integer> loadCartItem(/* Map<Product, Integer> cartItems */) {
        // if (cartItems.isEmpty()) {
        //     return null;
        // }
        Map<Product, Integer> testCartItems = new LinkedHashMap<>();
        testCartItems.put(new Coffee(1, "아메리카노", 2500),1);
        testCartItems.put(new Coffee(2, "카페라떼", 2500),2);
        testCartItems.put(new Coffee(3, "콜드브루", 3000),1);
        testCartItems.put(new Coffee(4, "카푸치노", 3000),2);
        testCartItems.put(new Coffee(5, "카페모카", 3000),4);
        testCartItems.put(new Coffee(6, "바닐라라떼", 4000),5);
        testCartItems.put(new Coffee(7, "아인슈페너", 3000),1);
        return testCartItems;
    }

    // 랜덤 주문목록 생성
    public void generateTestOrders(int count) {
        Random random = new Random();

        IntStream.range(0, count).forEach(i -> {
            Map<Product, Integer> testCartItems = new LinkedHashMap<>();

            // 7가지 커피 랜덤 수량 추가 (1~5개)
            testCartItems.put(new Coffee(1, "아메리카노", 2500), random.nextInt(5) + 1);
            testCartItems.put(new Coffee(2, "카페라떼", 2500), random.nextInt(5) + 1);
            testCartItems.put(new Coffee(3, "콜드브루", 3000), random.nextInt(5) + 1);
            testCartItems.put(new Coffee(4, "카푸치노", 3000), random.nextInt(5) + 1);
            testCartItems.put(new Coffee(5, "카페모카", 3000), random.nextInt(5) + 1);
            testCartItems.put(new Coffee(6, "바닐라라떼", 4000), random.nextInt(5) + 1);
            testCartItems.put(new Coffee(7, "아인슈페너", 3000), random.nextInt(5) + 1);

            createOrder(testCartItems);
        });
    }



}
