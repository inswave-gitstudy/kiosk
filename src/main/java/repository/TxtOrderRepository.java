package repository;

import model.Order;
import model.OrderStatus;
import model.Product;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TxtOrderRepository implements OrderRepository {
    private static final String BASE_DIR = "orders/"; // 기본 저장 폴더
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private String day;

    public TxtOrderRepository(String day) {
        this.day = day;
    }

    // 완료된 주문을 저장 (덮어쓰기 모드로 변경)
    public void saveOrder(Map<Integer, Order> orders) {
        LocalDate today = LocalDate.now();
        String yearDir = BASE_DIR + today.getYear() + "/";
        String monthDir = yearDir + String.format("%02d", today.getMonthValue()) + "/";
        String filePath = monthDir + String.format("%02d", today.getDayOfMonth()) + ".txt";

        // 폴더 생성
        File dir = new File(monthDir);
        if (!dir.exists() && !dir.mkdirs()) {
            System.out.println("폴더 생성 실패: " + monthDir);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) { // 덮어쓰기 모드
            boolean hasCompletedOrders = false;
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

            System.out.println("완료된 주문 저장됨: " + filePath);
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류 발생: " + e.getMessage());
        }
    }

    @Override
    public Map<Integer, Order> loadOrder() {
        Map<Integer, Order> orders = new LinkedHashMap<>();
        LocalDate date;

        try {
            date = LocalDate.parse(day, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        } catch (Exception e) {
            System.out.println("잘못된 날짜 형식: '" + day + "' (올바른 형식: YYYY/MM/DD)");
            return orders;
        }

        String yearDir = BASE_DIR + date.getYear() + "/";
        String monthDir = yearDir + String.format("%02d", date.getMonthValue()) + "/";
        String filePath = monthDir + String.format("%02d", date.getDayOfMonth()) + ".txt";

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println(day + "의 주문 파일이 존재하지 않습니다.");
            return orders;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains("|")) continue;
                Order order = txtToOrder(line);
                orders.put(order.getOrderId(), order);
            }
        } catch (IOException e) {
            System.out.println("파일 읽기 오류 발생: " + e.getMessage());
        }

        return orders;
    }

    private String orderToTxt(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append(order.getOrderId()).append("|");

        int totalPrice = 0;
        for (Map.Entry<Product, Integer> entry : order.getProducts().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            int price = product.getPrice() * quantity;
            totalPrice += price;

            sb.append(product.getProductId()).append(":")
                .append(product.getName()).append(":")
                .append(product.getPrice()).append(":")
                .append(quantity).append(",");
        }

        sb.append("|").append(totalPrice);
        sb.append("|").append(order.getStatus().name());
        sb.append("|").append(order.getDateTime().format(FORMATTER));

        return sb.toString();
    }

    private Order txtToOrder(String line) {
        String[] splits = line.split("\\|");
        int orderId = Integer.parseInt(splits[0]);
        int totalPrice = Integer.parseInt(splits[2]);
        OrderStatus status = OrderStatus.valueOf(splits[3]);
        LocalDateTime dateTime = LocalDateTime.parse(splits[4], FORMATTER);
        Map<Product, Integer> products = new HashMap<>();

        if (!splits[1].isEmpty()) {
            String[] strProducts = splits[1].split(",");
            for (String product : strProducts) {
                String[] productData = product.split(":");
                products.put(new Product(Integer.parseInt(productData[0]), productData[1], Integer.parseInt(productData[2])),
                    Integer.parseInt(productData[3]));
            }
        }
        return new Order(orderId, products, status, dateTime);
    }
}
