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
    public void saveOrder(Map<Integer, Order> orders) {
        Map<Integer, Order> sortedOrders = new LinkedHashMap<>(orders);
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(FILE_PATH, true)))) {
            for (Order order : sortedOrders.values()) {
                out.writeObject(order);  // 주문 객체 직렬화하여 파일에 저장
            }
            System.out.println("파일 저장 완료");
        } catch (IOException e) {
            System.out.println("파일 저장 오류");
        }
    }

    @Override
    public Map<Integer, Order> loadOrder() {
        Map<Integer, Order> orders = new LinkedHashMap<>();
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(FILE_PATH)))) {
            Object obj;
            while ((obj = in.readObject()) != null) {
                Order order = (Order) obj;
                orders.put(order.getOrderId(), order);
            }
        } catch (EOFException e) {
            // 파일 끝에 도달한 경우
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("파일이 없어 주문 내역을 읽어 올 수 없습니다.");
        }
        return orders;
    }

}
