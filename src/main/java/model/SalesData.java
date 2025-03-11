package model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

import manager.ProductManager;
import manager.SalesDataManager;

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
	public void loadFile(TreeMap<Integer, Order> orderList) {
		for(Map.Entry<Integer, Order> entry : orderList.entrySet()) {
			Order order = entry.getValue();
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
	
	//오버로딩(년도 통계에서 년도값에 대한 판매 물건들 초기화할 때 사용함)
	private void initializeDateItemMap(Integer date, Map map) {
		Map<Product, Integer> productSales = new HashMap<>();
		
		for (Product product : productList) {
			productSales.put(product, 0);
		}
		
		map.put(date, productSales);
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
	
	//오버로딩
	private void updateTransactionMap(Map<Product, Integer> value, Map<Product, Integer> map) {//추가할 상품들이 있는 맵, 추가할 곳
		for(Map.Entry<Product, Integer> quantity : value.entrySet()) {
			Product key = quantity.getKey();
			
			int prevValue = map.get(key);
			int addValue = quantity.getValue();
			
			map.put(key, addValue + prevValue);
		}
	}

	//년도별로 판매개수를 기록한 맵을 리턴
	public Map<Integer, Map<Product, Integer>> createYearSalesMap() {
		Map<Integer, Map<Product, Integer>> yearlySalesMap = new TreeMap<Integer, Map<Product,Integer>>();
		
		for(Map.Entry<LocalDate, Map<Product, Integer>> entry : transactionMap.entrySet()) {
			int year = entry.getKey().getYear(); //년도
			Map<Product, Integer> value = entry.getValue(); //판매개수
			
			if(!yearlySalesMap.containsKey(year)) {
				initializeDateItemMap(year, yearlySalesMap);
			}
			
			Map<Product, Integer> map = yearlySalesMap.get(year); // 저장할 곳에 있는 수량
			updateTransactionMap(value, map); //추가할 상품들이 있는 맵, 추가할 곳
		}
		
		return yearlySalesMap;
	}
	
	//월별로 판매개수를 기록한 맵을 리턴
	public Map<Integer, Map<Integer, Map<Product, Integer>>> createMonthlySalesMap() {
		Map<Integer, Map<Integer, Map<Product, Integer>>> monthlySalesMap = new TreeMap<Integer, Map<Integer,Map<Product,Integer>>>();
		
		for(Map.Entry<LocalDate, Map<Product, Integer>> entry : transactionMap.entrySet()) {
			int year = entry.getKey().getYear(); //년도
			int month = entry.getKey().getMonthValue(); //년도
			Map<Product, Integer> value = entry.getValue(); //판매개수
			
			//년도 초기화 추가
			if(!monthlySalesMap.containsKey(year)) {
				Map<Integer, Map<Product, Integer>> monthlyMap = new TreeMap<Integer, Map<Product,Integer>>();
				monthlySalesMap.put(year, monthlyMap);
			}
			//달에 대한 상품 초기화
			if(!monthlySalesMap.get(year).containsKey(month)) {
				initializeDateItemMap(month, monthlySalesMap.get(year));
			}
			
			//상품개수를 추가
			Map<Product, Integer> map = monthlySalesMap.get(year).get(month); // 저장할 곳에 있는 수량
			updateTransactionMap(value, map); //추가할 상품들이 있는 맵, 추가할 곳
		}
		
		return monthlySalesMap;
	}

	public Map<LocalDate, Map<Product, Integer>> getTransactionMap() {
		return transactionMap;
	}
}
