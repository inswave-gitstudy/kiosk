package manager;

import lombok.Getter;
import model.Coffee;
import model.Order;
import model.OrderStatus;
import model.Product;
import repository.OrderRepository;
import repository.RepositoryFactory;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class OrderManager {
    private Map<Integer, Order> orders; // 주문 목록 (주문번호 -> 주문)
    private int nextOrderId = 1; // 주문번호 증가를 위한 변수
    private final OrderRepository objectRepository;
    private final OrderRepository txtRepository;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1); // 자동 백업 스케줄러
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();  // 읽기-쓰기 락

    public OrderManager() {
        this.orders = new LinkedHashMap<>();
        this.objectRepository = RepositoryFactory.getRepository("object");
        this.txtRepository = RepositoryFactory.getRepository("txt");
    }

    // 주문 생성
    public void createOrder(Map<Product, Integer> cartItems) {
        lock.writeLock().lock();  // 쓰기 락을 걸어 주문 수정이 끝날 때까지 기다림
        Order order = new Order(nextOrderId++, cartItems);
        orders.put(order.getOrderId(), order);
        lock.writeLock().unlock();
    }

    // 주문 삭제
    public Order removeOrder(int orderId) {
        lock.writeLock().lock();
        Order remove = orders.remove(orderId);
        lock.writeLock().unlock();
        return remove;
    }

    // 주문 저장
    public void flushOrder() {
        lock.readLock().lock();
        try {
            if (!orders.isEmpty()) {  // 저장할 주문이 있을 때만 수행
                objectRepository.saveOrder(orders);  // 모든 주문 저장 (객체)
                txtRepository.saveOrder(getCompletedOrders());  // 완료된 주문 저장 (텍스트)
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    // 완료된 주문만 필터링
    private Map<Integer, Order> getCompletedOrders() {
        return orders.entrySet().stream()
            .filter(entry -> entry.getValue().getStatus() == OrderStatus.DONE)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // 주문 완료 처리
    public boolean completeOrder(int orderId) {
        lock.writeLock().lock();
        try {
            Order order = orders.get(orderId);
            if (order != null) {
                order.setStatus(OrderStatus.DONE);
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
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

    // 주문 목록 로드 ( 텍스트 파일 )
    public Map<Integer, Order> loadOrderWithTxt() {
        return txtRepository.loadOrder();
    }

    // 대기중인 주문만 조회
    public Map<Integer, Order> getPrepareOrder() {
        Map<Integer, Order> prepareOrders = new HashMap<>();
        for (Map.Entry<Integer, Order> entry : orders.entrySet()) {
            if (!entry.getValue().getStatus().equals(OrderStatus.DONE)) {
                prepareOrders.put(entry.getKey(), entry.getValue());
            }
        }
        return prepareOrders;
    }

    // 최근 10건 주문 가져오기
    public List<Order> getRecentOrder() {
        int size = orders.size();
        int startIndex = size >= 10 ? size - 10 : 0;
        return new ArrayList<>(orders.values()).subList(startIndex, size);
    }


    // 2시간마다 자동 백업 스케줄러 실행 - scheduler
    public void startBackupScheduler() {
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("[백업 시작] 주문 데이터를 저장합니다...");
            flushOrder();
            System.out.println("[백업 완료] 주문 데이터 저장 완료.");
        }, 0, 10, TimeUnit.MINUTES);
    }

    // 스케줄러 종료 - scheduler
    public void shutdownScheduler() {
        scheduler.shutdown();
    }

    // 가장 마지막 주문 조회 - order
    public Order getLastOrder() {
        return orders.get(Collections.max(orders.keySet()));
    }

    // 주문 영수증 저장
    public void saveReceipt() {
        Order lastOrder = getLastOrder();
        if (lastOrder != null) {
            txtRepository.saveReceipt(lastOrder);
        }
    }

    // ==================================================================== 안쓰는기능, 테스트 데이터 추가 기능

    // 랜덤 주문목록 생성 - order
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

    // 주문 목록 초기화
    public void clearOrder() {
        nextOrderId = 1;
        orders = new LinkedHashMap<>();
    }

    // 특정 주문 조회 (주문번호로 찾기) - order
    public Order getOrderById(int orderId) {
        return orders.get(orderId);
    }
}