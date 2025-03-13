package repository;

import model.Coffee;
import model.Order;
import model.OrderStatus;
import model.Product;
import utils.FilePathUtil;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// 통계 기능을 위해 주문상태가 [완료]처리된 주문 목록만을 텍스트 파일로 관리하는 저장소
public class TxtOrderRepository implements OrderRepository {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void saveOrder(Map<Integer, Order> newOrders) {
        // 기본 경로 설정
        String baseDir = FilePathUtil.getBaseDirectory();
        String filePath = baseDir + "orders.txt"; // orders.txt 파일을 기본 경로에 저장

        // 폴더가 존재하지 않으면 생성
        FilePathUtil.createDirectoryIfNotExists(baseDir); // 폴더가 존재하지 않으면 생성

        // 기존 주문 목록 불러오기
        Map<Integer, Order> existingOrders = loadOrder();

        // 새로운 주문 추가
        existingOrders.putAll(newOrders);

        // TreeMap 으로 변경하여 주문 번호 오름차순으로 정렬
        Map<Integer, Order> sortedOrders = new TreeMap<>(existingOrders);

        // 기존 데이터 + 새로운 데이터 다시 저장 (덮어쓰기)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Order order : sortedOrders.values()) {
                writer.write(orderToTxt(order));
                writer.newLine();
            }
            System.out.println("주문 내역 저장 완료 (" + filePath + ")");
        } catch (IOException e) {
            System.out.println("주문 내역 저장 오류: " + e.getMessage());
        }
    }

    @Override
    public Map<Integer, Order> loadOrder() {
        Map<Integer, Order> orders = new TreeMap<>();

        // 파일 경로 설정
        String filePath = FilePathUtil.getBaseDirectory() + "orders.txt"; // 고정된 orders.txt 파일

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("주문 파일이 존재하지 않습니다.");
            return orders;
        }

        // 주문 파일을 읽어오기
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

    // 객체 -> 문자열 파싱
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

    // 문자열 -> 객체 파싱
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

    // 영수증 출력용 (단건 주문)
    @Override
    public void saveReceipt(Order order) {
        String baseDir = FilePathUtil.getBaseDirectory();
        String directoryPath = baseDir + "receipts/"; // 영수증 저장 폴더
        String filePath = directoryPath + order.getOrderId() + "_receipt.txt"; // 주문번호_receipt.txt

        // 폴더가 존재하지 않으면 생성
        FilePathUtil.createDirectoryIfNotExists(directoryPath);  // 영수증 폴더가 존재하지 않으면 생성

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // 영수증 포맷 유지
            writer.write("\n========== [ 영수증 ] ==========\n");
            writer.write(String.format("주문번호: %-10d | 주문일시: %s\n", order.getOrderId(), order.getDateTime()));
            writer.write("---------------------------------\n");
            writer.write(String.format("%-15s %-5s %10s\n", "상품명", "수량", "가격"));
            writer.write("---------------------------------\n");

            for (Map.Entry<Product, Integer> entry : order.getProducts().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                int totalPrice = product.getPrice() * quantity;

                // 기본 상품 출력
                writer.write(String.format("%-15s %-5d %,10d원\n", product.getName(), quantity, totalPrice));

                // 커피 옵션 추가
                if (product instanceof Coffee) {
                    Coffee coffee = (Coffee) product;
                    writer.write(String.format("   [%s | %s | %s]\n",
                        coffee.isDecaf() ? "디카페인" : "카페인",
                        coffee.isIced() ? "아이스" : "핫",
                        coffee.getBeanType()));
                }
            }

            writer.write("---------------------------------\n");
            writer.write(String.format("총 수량: %-10d | 총 가격: %,10d원\n", order.getTotalAmount(), order.getTotalPrice()));
            writer.write(String.format("주문 상태: %s\n", order.getStatus().getStatus()));
            writer.write("=================================\n");

            System.out.println("영수증 저장 완료: " + filePath);
        } catch (IOException e) {
            System.out.println("영수증 저장 실패: " + e.getMessage());
        }
    }

}
