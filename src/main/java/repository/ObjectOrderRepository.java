package repository;

import model.Order;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class ObjectOrderRepository implements OrderRepository {
    private static final String FILE_PATH = "orders.ser";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void saveOrder(Map<Long, Order> orders) {
        Map<Long, Order> sortedOrders = new LinkedHashMap<>(orders);
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(FILE_PATH)))) {
            for (Order order : sortedOrders.values()) {
                out.writeObject(order);  // 주문 객체 직렬화하여 파일에 저장
            }
            System.out.println("주문이 성공적으로 저장되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<Long, Order> loadOrder() {
        Map<Long, Order> orders = new LinkedHashMap<>();
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(FILE_PATH)))) {
            Object obj;
            while ((obj = in.readObject()) != null) {
                Order order = (Order) obj;
                orders.put(order.getOrderId(), order);
            }
        } catch (EOFException e) {
            // 파일 끝에 도달한 경우
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return orders;
    }

}
