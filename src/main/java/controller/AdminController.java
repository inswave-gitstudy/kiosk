package controller;

import java.util.Scanner;

import manager.AdminManager;
import manager.ProductManager;

//관리자 메뉴 화면들 보여주는 곳
public class AdminController {
	private Scanner sc;
	private AdminManager adminManager;
	private OrderController orderController;
	private ProductManager productManager;
	private SalesDataController salesDataController;
	private int count;

	// 생성자
	public AdminController(ProductManager productManager, Scanner scanner, OrderController orderController,
			SalesDataController salesDataController) {
		this.orderController = orderController;
		this.adminManager = new AdminManager();
		this.productManager = productManager;
		this.sc = scanner;
		this.salesDataController = salesDataController;
		this.count = 0;
	}

	// 로그인 메뉴
	public void viewAdminLoginMenu() {
		count = 0;

		while (true) {
			System.out.print("관리자 비밀번호를 입력하세요>> ");
			String password = sc.nextLine().trim().replace(" ", "");

			if (adminManager.checkLoginCredentials(password)) {
				viewAdminMenu();
				break;
			} 
			else if(count == 2){
				System.out.println("3회 오류입니다");
				System.out.println("메인으로 돌아갑니다\n");
				break;
			}
			else {
				count++;
			}

			System.out.println(count + "회 오류입니다");
			System.out.println("3회 이상 오류시에 메인으로 돌아갑니다\n");
		}
	}

	// 관리자 메인 기능
	private void viewAdminMenu() {
		while (true) {
			printAdminMenu();
			String input = sc.nextLine().trim();
			System.out.println();

			switch (input) {
			case "1":
				productManager.run();
				break;
			case "2":
				salesDataController.viewSalesMenu();
				break;
			case "3":
				viewOrderMenu();
				break;
			case "4":
				adminManager.modifyPassword();
				break;
			case "5":
				return;
			default:
				System.out.println("잘못된 입력입니다");
			}
		}
	}

	// 관리자 메인 메뉴
	private void printAdminMenu() {
		System.out.println();
		System.out.println("------------관리자 화면-----------");
		System.out.println("1. 상품관리");
		System.out.println("2. 매출조회");
		System.out.println("3. 주문관리");
		System.out.println("4. 비밀번호 수정");
		System.out.println("5. 로그아웃");
		System.out.println("---------------------------------");
		System.out.print("번호를 입력하세요 >> ");
	}

	// 주문 관리 기능
	private void viewOrderMenu() {
		while (true) {
			printOrderMenu();
			String input = sc.nextLine().trim();
			System.out.println();

			switch (input) {
			case "1":
				orderController.cancelOrder();
				break;
			case "2":
				orderController.getAllOrder();
				break;
			case "3":
				orderController.getPrepareOrder();
				break;
			case "4":
				orderController.completeOrder();
				break;
			case "5":
				return;
			default:
				System.out.println("잘못된 입력입니다");
			}
		}
	}

	// 주문 관리 메뉴
	private void printOrderMenu() {
		System.out.println("------------매출 관리 화면-----------");
		System.out.println("1. 주문취소");
		System.out.println("2. 주문조회");
		System.out.println("3. 주문목록");
		System.out.println("4. 주문완료");
		System.out.println("5. 나가기");
		System.out.println("---------------------------------");
		System.out.print("번호를 입력하세요 >> ");
	}

}
