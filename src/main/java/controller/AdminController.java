package controller;
import java.util.Map;
import java.util.Scanner;

import manager.AdminManager;
import manager.OrderManager;
import manager.ProductManager;
import manager.SalesDataManager;
import model.Order;
import model.Product;

//관리자 메뉴 화면들 보여주는 곳
public class AdminController {
	Scanner sc;
	private AdminManager amdinManager;
	private OrderController orderController;
	private ProductManager productManager;
	private SalesDataController salesDataController;
	
	//생성자
    public AdminController(ProductManager productManager, Scanner scanner, OrderController orderController, SalesDataController salesDataController) {
    	this.orderController = orderController;
        this.amdinManager = new AdminManager();
        this.productManager = productManager;
        this.sc = scanner;
        this.salesDataController = salesDataController;
    }
    

    
    //로그인 테스트용
  	public void viewAdminLoginMenu() {
  		while(true) {
  			System.out.print("관리자 비밀번호를 입력하세요>> ");
  	  		String password = sc.nextLine().trim().replace(" ", "");
  	  		
  	  		if(amdinManager.checkLoginCredentials(password)) {
  	  			viewAdminMenu();
  	  			break;
  	  		}
  	  		
  	  		System.out.println("비밀번호가 일치하지 않습니다\n");
  		}
  	}
    
    //관리자 메인 기능
    public void viewAdminMenu() {
    	while(true) {
    		printAdminMenu();
    		String input = sc.nextLine();
    		System.out.println();
    		
    		switch(input) {
	    		case "1": productManager.run(); break;
	    		case "2": salesDataController.viewSalesMenu(); break;
	    		case "3": viewOrderMenu(); break;
	    		case "4": amdinManager.modifyPassword(); break;
	    		case "5": return;
	    		default: System.out.println("잘못된 입력입니다");
    		}
    	}
    }
    
    //관리자 메인 메뉴
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
   
    
   //주문 관리 기능
    private void viewOrderMenu() {
    	while(true) {
    		printOrderMenu();
    		String input = sc.nextLine();
    		System.out.println();
    		
    		switch(input) {
	    		case "1": //orderController.cancelOrder(input); 
	    				break;
	    		case "2": orderController.getAllOrder(); break;
	    		case "3": orderController.displayPrepareOrder(); break;
	    		case "4": viewAdminMenu();
	    		default: System.out.println("잘못된 입력입니다");
    		}
    	}
    }
    
    //주문취소
    
    
    
    //주문 관리 메뉴
    private void printOrderMenu() {
    	System.out.println("------------매출 관리 화면-----------");
    	System.out.println("1. 주문취소");
    	System.out.println("2. 주문조회");
    	System.out.println("3. 주문목록");
    	System.out.println("4. 나가기");
    	System.out.println("---------------------------------");
    	System.out.print("번호를 입력하세요 >> ");
    }
    
}
