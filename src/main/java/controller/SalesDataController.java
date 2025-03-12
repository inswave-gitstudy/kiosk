package controller;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import manager.OrderManager;
import manager.ProductManager;
import manager.SalesDataManager;
import model.Product;
import model.SalesData;

public class SalesDataController {
	private Scanner sc;
	private SalesDataManager salesDataManager;
	private SalesData salesData;

	// 생성자
	public SalesDataController(ProductManager productManager, OrderController orderController) {
		this.salesData = new SalesData(productManager, orderController);
		this.salesDataManager = new SalesDataManager(this.salesData);
		this.sc = new Scanner(System.in);
	}

	// 년, 월, 일에 대한 통계를 출력
	private void printSalesStats(String type) {
		TreeMap<String, Map<Integer, Integer>> salesStats = salesData.generateSalesMap(type);

		String title = "";
		String formatKey = "";

		switch (type) {
		case "YEAR":
			title = "연도별 매출 통계";
			formatKey = "%s년";
			break;
		case "MONTH":
			title = "월별 매출 통계";
			formatKey = "%s년 %s월";
			break;
		case "DAY":
			title = "일별 매출 통계";
			formatKey = "%s년 %s월 %s일";
			break;
		default:
			throw new IllegalArgumentException("Invalid type: " + type);
		}

		printDetailSalseStatus(salesStats, title, formatKey, type); // 통계 부분 출력
		printSaveFileMenu(type, salesStats); // 파일 저장 여부 묻기
	}

	//세부적으로 상품 통계를 출력(id, 상품명, 판매개수, 단가, 판매금액)
	private void printDetailSalseStatus(TreeMap<String, Map<Integer, Integer>> salesStats, String title, String formatKey,
			String type) {
		System.out.println("------------ " + title + " -----------");

		for (Map.Entry<String, Map<Integer, Integer>> entry : salesStats.entrySet()) {
			String key = entry.getKey(); //날짜
			Map<Integer, Integer> productSales = entry.getValue(); //해당 날짜에 상품과 판매개수

			int totalQuantity = 0; //총 판매개수
			int totalSalesAmount = 0; //총 매출

			if (type.equals("DAY")) {
				System.out.println("["
						+ String.format(formatKey, key.substring(0, 4), key.substring(4, 6), key.substring(6)) + "]");
			} else if (type.equals("MONTH")) {
				System.out.println("[" + String.format(formatKey, key.substring(0, 4), key.substring(4, 6)) + "]");
			} else {
				System.out.println("[" + String.format(formatKey, key) + "]");
			}

			System.out.printf("    [상품 ID]       [상품명]       [판매 개수]       [단가]       [판매 금액]\n");
			for (Map.Entry<Integer, Integer> productEntry : productSales.entrySet()) {
				int productId = productEntry.getKey();//상품ID
				int quantity = productEntry.getValue(); //판매개수

				// Product 객체 가져와서 가격 및 이름 조회
				Product product = salesData.getProductById(productId);
				int price = (product != null) ? product.getPrice() : 0;
				String productName = (product != null) ? product.getName() : "Unknown Product";

				totalQuantity += quantity;
				totalSalesAmount += price * quantity;

				System.out.printf("   ▶%5d  %10s  %12d개  %12d원  %12d원\n", productId, productName,
						quantity, price, price * quantity);
			}

			System.out.printf("   [총 판매 개수]: %d개\n", totalQuantity);
			System.out.printf("   [총 매출 금액]: %d원\n", totalSalesAmount);
			System.out.println("-------------------------------------");
		}
	}

	//통계 파일저장 여부 메뉴
	private void printSaveFileMenu(String type, TreeMap<String, Map<Integer, Integer>> salesStats) {
		while (true) {
			System.out.print("해당 정보를 저장하시겠습니까(y, n)>> ");
			String input = sc.nextLine();

			switch (input) {
			case "y":
			case "Y":
				salesDataManager.saveFileToCSV(salesStats, type);
				return;
			case "n":
			case "N":
				System.out.println("저장이 취소되었습니다.");
				return;
			default:
				System.out.println("잘못된 입력입니다.");
				break;
			}
		}
	}

	// 매출 조회 기능
	public void viewSalesMenu() {
		while (true) {
			printSalesMenu();
			String input = sc.nextLine();
			System.out.println();

			switch (input) {
			case "1":
				printSalesStats("YEAR");
				break;
			case "2":
				printSalesStats("MONTH");
				break;
			case "3":
				printSalesStats("DAY");
				break;
			case "4":
				return;
			default:
				System.out.println("잘못된 입력입니다");
			}
		}

	}

	// 매출 조회 메뉴
	private void printSalesMenu() {
		System.out.println("------------매출 관리 화면-----------");
		System.out.println("1. 년도별 매출 통계");
		System.out.println("2. 월별 매출 통계");
		System.out.println("3. 일별 매출 통계");
		System.out.println("4. 나가기");
		System.out.println("---------------------------------");
		System.out.print("번호를 입력하세요 >> ");
	}
}
