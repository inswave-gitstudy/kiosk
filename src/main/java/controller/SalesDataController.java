package controller;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

import manager.ProductManager;
import manager.SalesDataManager;
import model.Order;
import model.Product;
import model.SalesData;

public class SalesDataController {
	private ProductManager productManager; 
	private SalesDataManager salesDataManager;
	private SalesData salesData;
	private Scanner sc;

	public SalesDataController() {
		this.productManager = new ProductManager();
		this.salesDataManager = new SalesDataManager();
		this.salesData = new SalesData();
		this.sc = new Scanner(System.in);
	}

	// 테스트용 생성자
	public SalesDataController(List<Product> productList, SalesData salesData) {
		this.productManager = new ProductManager();
		this.salesDataManager = new SalesDataManager();
		this.salesData = salesData;
		this.sc = new Scanner(System.in);
	}
	
	
	//일별로 출력
	public void getDailyReport() {
		Map <LocalDate, Map<Product, Integer>>DailySalesMap = salesData.getTransactionMap();

		System.out.println("=============== 키오스크 일별 판매 통계 ===============");
		for(Entry<LocalDate, Map<Product, Integer>> entry : DailySalesMap.entrySet()) {
			LocalDate date = entry.getKey();
			int year = date.getYear();
			int month = date.getMonthValue();
			int day = date.getDayOfMonth();
			Map<Product, Integer> productTotal = entry.getValue();
		
			System.out.println("[" + year + "년 " + month + "월 " + day + "일]");
			System.out.println("-----------------------------------------------------");
			printProductReport(productTotal);
		}
		
		printSaveSalesReportToFile(DailySalesMap, "day");//파일 저장여부
	}
	
	//월별로 출력
	public void getMonthlyReport() {
		Map<Integer, Map<Integer, Map<Product, Integer>>> monthlySalesMap = salesData.createMonthlySalesMap();
		
		System.out.println("=============== 키오스크 연도별 판매 통계 ===============");
		
		for(Map.Entry<Integer, Map<Integer, Map<Product, Integer>>> entry : monthlySalesMap.entrySet()) {
			Map<Integer, Map<Product, Integer>> map = entry.getValue();
			int year = entry.getKey();
	
	        System.out.println("[" + year + "년]");
	        
	        for(Map.Entry<Integer, Map<Product, Integer>> monthly : map.entrySet()) {
	        	Map<Product, Integer> products = monthly.getValue();
				int month = monthly.getKey();
				System.out.println("[" + month + "월]");
	        	System.out.println("-----------------------------------------------------");
	        	printProductReport(products);
	        }
		}
		
		printSaveSalesReportToFile(monthlySalesMap, "month");//파일 저장여부
	}
	
	//연도별 통계를 출력함
	public void getYearReport() {
		Map<Integer, Map<Product, Integer>> yearlySalesMap = salesData.createYearSalesMap();
		
		System.out.println("=============== 키오스크 연도별 판매 통계 ===============");
		
		for(Map.Entry<Integer, Map<Product, Integer>> entry : yearlySalesMap.entrySet()) {
			Map<Product, Integer> products = entry.getValue();
			int year = entry.getKey();
	
	        System.out.println("[" + year + "년]");
	        System.out.println("-----------------------------------------------------");
	        printProductReport(products);
		}
		
		printSaveSalesReportToFile(yearlySalesMap, "year");//파일 저장여부
	}
	
	
	
	//상품을 출력함
	public void printProductReport(Map<Product, Integer> products) {
		System.out.println("[상품명]\t\t[가격]\t\t[수량]\t\t[매출]");
        System.out.println("-----------------------------------------------------");

        int totalCount = 0;
        int totalSales = 0;
        
		for(Map.Entry<Product, Integer> value : products.entrySet()) {
			Product name = value.getKey(); //음식명
            int count = value.getValue();  //판매개수
            int price = name.getPrice();   //가격
            int sales = price * count;     //매출

            System.out.printf("%-10s\t%,2d원\t%,10d개\t%,10d원\n", 
                      name.getName(), price, count, sales);

            totalCount += count;
            totalSales += sales;
		}
		System.out.println("-----------------------------------------------------");
		System.out.printf("총 판매 개수: %d개\n", totalCount);
		System.out.printf("총 매출: %d원\n\n", totalSales);
	}
	
	
	//파일 저장 여부 묻고, 저장하는 기능
	public void printSaveSalesReportToFile(Map map, String str){
		System.out.println();
		
		while(true) {
			System.out.print("파일로 저장하시겠습니까(y, n)>> ");
			String input = sc.nextLine();
			
			switch (input) {
				case "y":
				case "Y": 
					if(str.equals("year"))
						salesDataManager.saveYearSalesReportToFile(map);
					else if(str.equals("month"))
						salesDataManager.saveMonthlySalesReportToFile(map);
					else
						salesDataManager.saveDailySalesReportToFile(map);
					return;
				case "n":
				case "N": return;
				default: System.out.println("잘못 입력하셨습니다");
			}
		}
		
	} 
	
}