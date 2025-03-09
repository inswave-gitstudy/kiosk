package repository;

import model.Order;
import model.OrderStatus;
import model.Product;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

public class TxtOrderRepository implements OrderRepository {
    private static final String BASE_DIR = "orders/"; // 기본 저장 폴더
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 날짜별 완료 주문 저장 (폴더 구조: orders/YYYY/MM/DD.txt)
    public void saveOrder(Map<Integer, Order> orders) {
        LocalDate today = LocalDate.now();
        String yearDir = BASE_DIR + today.getYear() + "/"; // "orders/2025/"
        String monthDir = yearDir + String.format("%02d", today.getMonthValue()) + "/"; // "orders/2025/03/"
        String filePath = monthDir + String.format("%02d", today.getDayOfMonth()) + ".txt"; // "orders/2025/03/09.txt"

        // 폴더 생성 (존재하지 않으면 생성)
        File dir = new File(monthDir);
        if (!dir.exists() && !dir.mkdirs()) {
            System.out.println("폴더 생성 실패: " + monthDir);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) { // 기존 파일에 추가 저장
            boolean hasCompletedOrders = false;

            // 주문 ID 기준으로 정렬
            TreeMap<Integer, Order> sortedOrders = new TreeMap<>(orders);

            for (Order order : sortedOrders.values()) {
                if (order.getStatus() == OrderStatus.DONE) {
                    writer.write(orderToTxt(order));
                    writer.newLine();
                    hasCompletedOrders = true;
                }
            }

            if (!hasCompletedOrders) {
                writer.write("완료된 주문이 없습니다.\n");
            }

            System.out.println("완료된 주문이 저장됨: " + filePath);
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류 발생: " + e.getMessage());
        }
    }

    @Override
    public Map<Integer, Order> loadOrder() {
        return null;
    }

    // 주문을 텍스트 형식으로 변환
    private String orderToTxt(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append(order.getOrderId()).append("|");

        int totalPrice = 0;
        Map<Product, Integer> products = order.getProducts();
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            int price = product.getPrice() * quantity;
            totalPrice += price;

            sb.append(product.getProductId()).append(":") // productId
                .append(product.getName()).append(":") // name
                .append(product.getPrice()).append(":") // price
                .append(quantity).append(","); // amount
        }

        sb.append("|").append(totalPrice); // 총 금액 추가
        sb.append("|").append(order.getStatus().name()); // 주문 상태
        sb.append("|").append(order.getDateTime().format(FORMATTER)); // 주문 날짜
        return sb.toString();
    }
}
