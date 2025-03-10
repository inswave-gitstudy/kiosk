package manager;

import lombok.Getter;
import model.Coffee;
import model.Order;
import model.OrderStatus;
import model.Product;
import repository.ObjectOrderRepository;
import repository.OrderRepository;
import repository.TxtOrderRepository;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class OrderManager {
    private Map<Integer, Order> orders; // 주문 목록 (주문번호 -> 주문)
    private int nextOrderId = 1; // 주문번호 증가를 위한 변수
    private final OrderRepository objectRepository; // 모든 주문 저장소 (자바 객체)
    private final OrderRepository txtRepository; // 완료된 주문 저장소 (텍스트 파일)
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1); // 자동 백업 스케줄러

    public OrderManager() {
        this.orders = new LinkedHashMap<>();
        this.objectRepository = new ObjectOrderRepository();
        this.txtRepository = new TxtOrderRepository(null);
        startBackupScheduler(); // 2시간마다 자동 백업 실행
    }


    // 주문 생성
    public void createOrder(Map<Product, Integer> cartItems) {
        Order order = new Order(nextOrderId++, cartItems); // 주문 생성
        orders.put(order.getOrderId(), order); // 주문 저장

    }

    // 주문 삭제 (파일 저장은 일정 시간마다 수행)
    public Order removeOrder(int orderId) {
        return orders.remove(orderId);
    }

    // 주문 목록 초기화
    private void clearOrder() {
        nextOrderId = 1;
        orders = new LinkedHashMap<>();
    }

    // 주문 저장
    public void flushOrder() {
        if (!orders.isEmpty()) {  // 저장할 주문이 있을 때만 수행
            objectRepository.saveOrder(orders); // 모든 주문 저장 (객체)
            txtRepository.saveOrder(getCompletedOrders()); // 완료된 주문 저장 (텍스트)
        }
    }

    // 완료된 주문만 필터링
    public Map<Integer, Order> getCompletedOrders() {
        return orders.entrySet().stream()
            .filter(entry -> entry.getValue().getStatus() == OrderStatus.DONE)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
            flushOrder(); // 완료 즉시 저장
            return true;
        }
        return false;
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

    // 주문 목록 로드 (최대 주문 ID 갱신)
    public void loadOrder() {
        Map<Integer, Order> loadedOrders = objectRepository.loadOrder();
        if (!loadedOrders.isEmpty()) {
            this.orders.putAll(loadedOrders);
            this.nextOrderId = Collections.max(orders.keySet()) + 1; // 마지막 주문 ID + 1
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
    /*
    public Map<Product, Integer> loadCartItem(Map<Product, Integer> cartItems) {
        if (cartItems.isEmpty()) {
            return null;
        }
        createOrder(cartItems);
        Map<Product, Integer> testCartItems = new LinkedHashMap<>();
        testCartItems.put(new Coffee(1, "아메리카노", 2500), 1);
        testCartItems.put(new Coffee(2, "카페라떼", 2500), 2);
        testCartItems.put(new Coffee(3, "콜드브루", 3000), 1);
        testCartItems.put(new Coffee(4, "카푸치노", 3000), 2);
        testCartItems.put(new Coffee(5, "카페모카", 3000), 4);
        testCartItems.put(new Coffee(6, "바닐라라떼", 4000), 5);
        testCartItems.put(new Coffee(7, "아인슈페너", 3000), 1);
        return testCartItems;
    }
    */

    // 랜덤 주문목록 생성
    public void generateTestOrders(int count) {
        Random random = new Random();
        String[] beanTypes = {"에티오피아", "콜롬비아", "과테말라", "브라질", "케냐"}; // 원두 종류

        IntStream.range(0, count).forEach(i -> {
            Map<Product, Integer> testCartItems = new LinkedHashMap<>();

            // 7가지 커피 랜덤 수량 & 옵션 추가
            testCartItems.put(createRandomCoffee(1, "아메리카노", 2500, random, beanTypes), random.nextInt(5) + 1);
            testCartItems.put(createRandomCoffee(2, "카페라떼", 2500, random, beanTypes), random.nextInt(5) + 1);
            testCartItems.put(createRandomCoffee(3, "콜드브루", 3000, random, beanTypes), random.nextInt(5) + 1);
            testCartItems.put(createRandomCoffee(4, "카푸치노", 3000, random, beanTypes), random.nextInt(5) + 1);
            testCartItems.put(createRandomCoffee(5, "카페모카", 3000, random, beanTypes), random.nextInt(5) + 1);
            testCartItems.put(createRandomCoffee(6, "바닐라라떼", 4000, random, beanTypes), random.nextInt(5) + 1);
            testCartItems.put(createRandomCoffee(7, "아인슈페너", 3000, random, beanTypes), random.nextInt(5) + 1);

            createOrder(testCartItems);
        });
    }

    // 랜덤한 커피 옵션을 가진 Coffee 객체 생성
    private Coffee createRandomCoffee(int id, String name, int price, Random random, String[] beanTypes) {
        Coffee coffee = new Coffee(id, name, price);
        coffee.setDecaf(random.nextBoolean()); // 디카페인 여부 랜덤
        coffee.setIced(random.nextBoolean()); // 아이스 여부 랜덤
        coffee.setBeanType(beanTypes[random.nextInt(beanTypes.length)]); // 원두 종류 랜덤
        return coffee;
    }

    // 2시간마다 자동 백업 스케줄러 실행
    private void startBackupScheduler() {
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("[백업 시작] 주문 데이터를 저장합니다...");
            flushOrder();
            System.out.println("[백업 완료] 모든 주문(객체) 및 완료된 주문(텍스트) 저장 완료.");
        }, 0, 2, TimeUnit.HOURS);
    }

    // 프로그램 종료 시 스케줄러 종료
    public void shutdownScheduler() {
        scheduler.shutdown();
    }
}
