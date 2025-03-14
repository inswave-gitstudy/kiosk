package model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import controller.OrderController;
import manager.ProductManager;

public class SalesData {
	private Map<Integer, Product> productMap; //상품정보
	private OrderController orderController;

	//생성자
	public SalesData(ProductManager productManager, OrderController orderController) {
		this.productMap = new HashMap<>();
		this.orderController = orderController;

		//상품 맵
		for (Product product : productManager.getAllProducts()) {
			productMap.put(product.getProductId(), product);
		}
	}

	//상품 맵에서 id 값으로 정보를 조회
	public Product getProductById(int productId) {
		return productMap.get(productId);
	}

	//통계 맵을 생성함
	public TreeMap<String, Map<Integer, Integer>> generateSalesMap(String type) {
		TreeMap<String, Map<Integer, Integer>> salesStats = new TreeMap<>();

		// 모든 주문 가져오기
		Map<Integer, Order> allOrders = orderController.loadOrderWithTxt();

		for (Order order : allOrders.values()) {
			LocalDateTime orderTime = order.getDateTime();
			
			String key = "";

			// 통계 유형별 키 생성 (연도별, 월별, 일별)
			switch (type) {
			case "YEAR":
				key = String.format("%04d", orderTime.getYear()); // YYYY
				break;
			case "MONTH":
				key = String.format("%04d%02d", orderTime.getYear(), orderTime.getMonthValue()); // YYYYMM
				break;
			case "DAY":
				key = String.format("%04d%02d%02d", orderTime.getYear(), orderTime.getMonthValue(), orderTime.getDayOfMonth()); // YYYYMMDD
				break;
			default:
				throw new IllegalArgumentException("[통계]잘못된 날짜 타입: " + type);
			}

			//putIfAbsent란 해당 키값이 없을 때 value 값을 넣어줌
			salesStats.putIfAbsent(key, new HashMap<>());
			Map<Integer, Integer> productSales = salesStats.get(key);

			// 주문된 상품 리스트 가져오기
			for (Map.Entry<Product, Integer> entry : order.getProducts().entrySet()) {
				int productId = entry.getKey().getProductId(); // Product ID
				int quantity = entry.getValue(); // 판매 개수

				// 상품 판매 개수 업데이트
				productSales.put(productId, productSales.getOrDefault(productId, 0) + quantity);
			}
		}

		return salesStats;//해당 맵을 리턴함
	}
	
	
	
	public TreeMap<String, Map<Integer, Integer>> generateSalesMap(String type, String startDate, String endDate) {
		TreeMap<String, Map<Integer, Integer>> salesStats = new TreeMap<>();

		// 모든 주문 가져오기
		Map<Integer, Order> allOrders = orderController.loadOrderWithTxt();

		for (Order order : allOrders.values()) {
			LocalDateTime orderTime = order.getDateTime();
			
			// startDate와 endDate를 LocalDateTime으로 변환
			LocalDateTime start = LocalDateTime.parse(startDate.substring(0, 4) + "-" + startDate.substring(4, 6) + "-" + startDate.substring(6) + "T00:00:00");
			LocalDateTime end = LocalDateTime.parse(endDate.substring(0, 4) + "-" + endDate.substring(4, 6) + "-" + endDate.substring(6) + "T23:59:59");

			// orderTime이 start와 end 사이에 있는지 확인
			if (orderTime.isBefore(start) || orderTime.isAfter(end)) {
			    continue; // 범위 밖이면 continue
			}
			
			String key = "";

			// 통계 유형별 키 생성 (연도별, 월별, 일별)
			switch (type) {
			case "YEAR":
				key = String.format("%04d", orderTime.getYear()); // YYYY
				break;
			case "MONTH":
				key = String.format("%04d%02d", orderTime.getYear(), orderTime.getMonthValue()); // YYYYMM
				break;
			case "DAY":
				key = String.format("%04d%02d%02d", orderTime.getYear(), orderTime.getMonthValue(), orderTime.getDayOfMonth()); // YYYYMMDD
				break;
			default:
				throw new IllegalArgumentException("[통계]잘못된 날짜 타입: " + type);
			}

			//putIfAbsent란 해당 키값이 없을 때 value 값을 넣어줌
			salesStats.putIfAbsent(key, new HashMap<>());
			Map<Integer, Integer> productSales = salesStats.get(key);

			// 주문된 상품 리스트 가져오기
			for (Map.Entry<Product, Integer> entry : order.getProducts().entrySet()) {
				int productId = entry.getKey().getProductId(); // Product ID
				int quantity = entry.getValue(); // 판매 개수

				// 상품 판매 개수 업데이트
				productSales.put(productId, productSales.getOrDefault(productId, 0) + quantity);
			}
		}

		return salesStats;//해당 맵을 리턴함
	}
}
