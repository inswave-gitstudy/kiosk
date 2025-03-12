package repository;

import model.Order;
import utils.FilePathUtil;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class ObjectOrderRepository implements OrderRepository {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void saveOrder(Map<Integer, Order> newOrders) {
        // 기본 경로 설정
        String baseDir = FilePathUtil.getBaseDirectory();
        String filePath = baseDir + "orders.ser"; // orders.ser 파일을 기본 경로에 저장

        // 기존 주문 목록 불러오기
        Map<Integer, Order> existingOrders = loadOrder(filePath);
        existingOrders.putAll(newOrders); // 새로운 주문 추가

        // 폴더 없을 경우 생성 로직 추가
        FilePathUtil.createDirectoryIfNotExists(baseDir);

        // 기존 데이터 + 새로운 데이터 다시 저장 (덮어쓰기)
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filePath)))) {
            out.writeObject(existingOrders);
            System.out.println("주문 내역 저장 완료 (" + filePath + ")");
        } catch (IOException e) {
            System.out.println("주문 내역 저장 오류: " + e.getMessage());
        }
    }

    @Override
    public Map<Integer, Order> loadOrder() {
        // 기본 경로 설정
        String baseDir = FilePathUtil.getBaseDirectory();
        String filePath = baseDir + "orders.ser"; // orders.ser 파일을 기본 경로에서 로드

        return loadOrder(filePath);
    }

    public Map<Integer, Order> loadOrder(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("주문 내역 파일 없음");
            return new LinkedHashMap<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filePath)))) {
            return (Map<Integer, Order>) in.readObject();
        } catch (EOFException e) {
            System.out.println("주문 내역이 비어 있습니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("주문 내역 로드 오류: " + e.getMessage());
        }
        return new LinkedHashMap<>();
    }
}
