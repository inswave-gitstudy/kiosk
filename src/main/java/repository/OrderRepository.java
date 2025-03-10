package repository;

import model.Order;

import java.util.Map;

public interface OrderRepository {
    // 직렬화를 통한 파일 백업 관리용
    void saveOrder(Map<Integer, Order> orders);

    // 역직렬화를 통한 파일 백업 관리용
    Map<Integer, Order> loadOrder();

    // 특정 날자 주문 목록 조회용
    default Map<Integer, Order> loadOrder(String day) {
        System.out.println("특정 날자 주문 목록 조회용으로 오버라이딩해야함");
        return Map.of();
    }
}
