package repository;

import model.Order;

import java.util.Map;

public interface OrderRepository {
    // 직렬화를 통한 파일 백업 관리용
    void saveOrder(Map<Integer, Order> orders);
    // 역직렬화를 통한 파일 백업 관리용
    Map<Integer, Order> loadOrder();
}
