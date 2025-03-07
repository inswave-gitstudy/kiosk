package repository;

import model.Order;
import model.OrderStatus;
import model.Product;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class TxtOrderRepository implements OrderRepository {
    private static final String FILE_PATH = "orders.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void saveOrder(Map<Long, Order> orders) {
        TreeMap<Long, Order> sortedOrders = new TreeMap<>(orders);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Order order : sortedOrders.values()) {
                writer.write(orderToTxt(order));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<Long, Order> loadOrder() {
        Map<Long, Order> orders = new LinkedHashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Order order = txtToOrder(line);
                orders.put(order.getOrderId(), order);
            }
        } catch (IOException e) {
            return null;
        }
        return orders;
    }

    // 1 | 아메리카노:2000:5,케이크:5000:1 | 준비중 | 2021/11/11 15:00
    // [주문번호]|[productId]|[name]:[price]:[amount], .... |PREPARE|[dateTime]
    private String orderToTxt(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append(order.getOrderId()).append("|");

        Map<Product, Integer> products = order.getProducts();
        for (Product product : products.keySet()) {
            sb.append(product.getProductId()).append(":") // productId
                .append(product.getName()).append(":") // name
                .append(product.getPrice()).append(":") // price
                .append(products.get(product)) // amount
                .append(",");
        }
        sb.append("|").append(order.getStatus().name()); // status
        sb.append("|").append(order.getDateTime().format(FORMATTER)); // dateTime
        return sb.toString();
    }

    // [주문번호]|[productId]|[name]:[price]:[amount], .... |PREPARE|[dateTime]

    private Order txtToOrder(String line) {
        String[] splits = line.split("\\|");
        long orderId = Integer.parseInt(splits[0]);
        OrderStatus status = OrderStatus.valueOf(splits[2]);
        LocalDateTime dateTime = LocalDateTime.parse(splits[3], FORMATTER);
        Map<Product, Integer> products = new HashMap<>();
        // [productId]|[name]:[price]:[amount], ....
        if (!splits[1].isEmpty()) {
            String[] productsArr = splits[1].split(",");
            for (String s : productsArr) {
                String[] productData = s.split(":");
                products.put(
                    new Product(Long.parseLong(productData[0]), productData[1],
                        Integer.parseInt(productData[2])), Integer.parseInt(productData[3]));
            }
        }
        return new Order(orderId, products, status, dateTime);
    }
}
