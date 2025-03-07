package controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import manager.ProductManager;
import model.Order;
import model.Product;

public class SalesData {
	private Map<LocalDate, Map<Product, Integer>> transactionMap; //날짜, Product, 판매수량
	private ProductManager productManager; 
	private List<Product> productList; //음식 리스트

	public SalesData() {
		this.transactionMap = new TreeMap<>();
		this.productManager = new ProductManager();
		this.productList = productManager.getAllProducts();
	}

	// 테스트용 생성자
	public SalesData(List<Product> productList) {
		this.transactionMap = new TreeMap<>();
		this.productManager = new ProductManager();
		this.productList = productList;
	}

	// 파일 불러오기
	public void loadFile(TreeSet<Order> orderList) {
		Iterator<Order> it = orderList.iterator();
		while (it.hasNext()) {
			Order order = it.next();
			LocalDate date = order.getDateTime().toLocalDate();

			if (!transactionMap.containsKey(date)) {
				initializeDateItemMap(date); //제품 초기화
			}
			updateTransactionMap(date, order);
		}
	}

	// 특정 날짜에 대한 제품 판매량 초기화(처음에 날짜 데이터값 없을 때)
	private void initializeDateItemMap(LocalDate date) {
		Map<Product, Integer> productSales = new HashMap<>();
		for (Product product : productList) {
			productSales.put(product, 0);
		}
		transactionMap.put(date, productSales);
	}

	// 주문 정보 업데이트
	private void updateTransactionMap(LocalDate date, Order order) {
		Map<Product, Integer> dailySales = transactionMap.get(date);
		
		for (Map.Entry<Product, Integer> entry : order.getProducts().entrySet()) {
			Product product = entry.getKey();
			
			int prevValue = dailySales.get(product);//이전에 저장되어 있던 개수
			int addValue = entry.getValue();//새롭게 추가할 개수
			
			dailySales.put(product, prevValue + addValue);
		}
	}

	//년도별 통계
	public void getAnnualReport() {
		System.out.println();
		
	}
	
	//월별 통계
	public void getMonthlyReport() {
		
	}
	
	//일별 통계
	public void getDailyReport() {
		System.out.println("===================날짜별 매출 통계======================");
		for (Map.Entry<LocalDate, Map<Product, Integer>> entry : transactionMap.entrySet()) {
			LocalDate date = entry.getKey();
			Map<Product, Integer> productMap = entry.getValue();
			int totalDailyCount = 0;
			int totalDailySales =0;
		
			System.out.println("======================================================");
			System.out.println("[날짜: " + date + "]");
			System.out.println("[상품명]\t\t[가격]\t\t[수량]\t\t[매출]\t\t");
			
			for (Map.Entry<Product, Integer> productEntry : productMap.entrySet()) {
				StringBuffer bf = new StringBuffer();
				String name = productEntry.getKey().getName(); //음식이름
				int price = productEntry.getKey().getPrice(); //음식가격
				int count = productEntry.getValue(); //음식 판매 개수
				int sales = price*count; // 매출
				
				totalDailyCount += count; //일별 판매수량 계산
				totalDailySales += price*count; //일별 매출 계산
				
				bf.append(name + "\t\t");
				bf.append(price + "원\t\t");
				bf.append(count + "개\t\t");
				bf.append(sales + "원\t\t");
				
				System.out.println(bf);
			}
			
			System.out.println("------------------------------------------------------");
			System.out.println("[총 판매 수량]: " + totalDailyCount + "개");
			System.out.println("[총 매출]: " + totalDailySales + "원");
		}
		System.out.println("======================================================");
	}
}
