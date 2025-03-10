package controller;
import java.util.Map;
import java.util.Scanner;

import manager.AdminManager;
import manager.OrderManager;
import model.Order;
import model.Product;

//관리자 메뉴 화면들 보여주는 곳
public class AdminController {
	Scanner sc;
	private SalesDataController salesdata;
	private AdminManager amdinManager;
	private OrderManager orderManager;
	
	//생성자
    public AdminController() {
        this.salesdata = new SalesDataController();
        this.amdinManager = new AdminManager();
        this.orderManager = new OrderManager();
        this.sc = new Scanner(System.in);
    }
    
  //생성자 테스트용
    public AdminController(OrderManager order) {
        this.salesdata = new SalesDataController();
        this.amdinManager = new AdminManager();
        this.orderManager = order;
        this.sc = new Scanner(System.in);
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
	    		case "1": viewFoodMenu(); break;
	    		case "2": viewSalesMenu(); break;
	    		case "3": viewOrderMenu(); break;
	    		case "4": amdinManager.modifyPassword(); break;
	    		case "5": viewAdminLoginMenu();
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
   
    
    //상품관리 기능
    private void viewFoodMenu() {
    	while(true) {
    		printFoodMenu();
    		String input = sc.nextLine();
    		System.out.println();
    		
    		switch(input) {
	    		case "1": System.out.println("상품추가"); break;
	    		case "2": System.out.println("상품삭제"); break;
	    		case "3": System.out.println("상품조회"); break;
	    		case "4": System.out.println("상품수정"); break;
	    		default: System.out.println("잘못된 입력입니다");
    		}
    	}
    }
    
    //상품관리 메뉴
    private void printFoodMenu() {
    	System.out.println("------------상품 관리 화면-----------");
    	System.out.println("1. 상품추가");
    	System.out.println("2. 상품삭제");
    	System.out.println("3. 상품조회");
    	System.out.println("4. 상품수정");
    	System.out.println("5. 나가기");
    	System.out.println("---------------------------------");
    	System.out.print("번호를 입력하세요 >> ");
    }
    
    
    //매출 조회 기능
    private void viewSalesMenu() {
    	while(true) {
    		printSalesMenu();
    		String input = sc.nextLine();
    		System.out.println();
    		
    		switch(input) {
	    		case "1": salesdata.getYearReport(); break;
	    		case "2": salesdata.getMonthlyReport(); break;
	    		case "3": salesdata.getDailyReport(); break;
	    		case "4": viewAdminMenu();
	    		default: System.out.println("잘못된 입력입니다");
    		}
    	}
    	
    }
    
    //매출 조회 메뉴
    private void printSalesMenu() {
    	System.out.println("------------매출 관리 화면-----------");
    	System.out.println("1. 년도별 매출 통계");
    	System.out.println("2. 월별 매출 통계");
    	System.out.println("3. 일별 매출 통계");
    	System.out.println("4. 나가기");
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
	    		case "1": printCancelOrder(); break;
	    		case "2": PrintOrderList(orderManager.getAllOrder()); break;
	    		case "3": PrintOrderList(orderManager.getPrePareOrder()); break;
	    		case "4": viewAdminMenu();
	    		default: System.out.println("잘못된 입력입니다");
    		}
    	}
    }

    private void printCancelOrder() {//주문취소
    	System.out.print("취소할 주문ID를 입력하세요>> ");
    	int input = Integer.parseInt(sc.nextLine());
    	
    	if(orderManager.cancelOrder(input))
    		System.out.println("주문이 취소되었습니다");
    	else
    		System.out.println("주문취소를 실패했습니다");
    }
    
    private void PrintOrderList(Map<Integer, Order> orderMap) {//주문조회
    	Map<Integer, Order> orders = orderMap;
    	
    	for(Map.Entry<Integer, Order> entry : orders.entrySet()) {
    		System.out.println(entry.getValue());
    	}
    }
    
    
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
