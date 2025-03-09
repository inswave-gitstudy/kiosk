package controller;
import java.util.Scanner;

import manager.AdminManager;

//관리자 메뉴 화면들 보여주는 곳
public class AdminController {
	Scanner sc;
	private SalesDataController salesdata;
	private AdminManager amdinManager;
	
	//생성자
    public AdminController() {
        this.salesdata = new SalesDataController();
        this.amdinManager = new AdminManager();
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
	    		case "2": viewSales(); break;
	    		case "3": amdinManager.modifyPassword(); break;
	    		case "4": viewAdminLoginMenu();
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
    	System.out.println("3. 비밀번호 수정");
    	System.out.println("4. 로그아웃");
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
    private void viewSales() {
    	while(true) {
    		printSales();
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
    private void printSales() {
    	System.out.println("------------매출 관리 화면-----------");
    	System.out.println("1. 년도별 매출 통계");
    	System.out.println("2. 월별 매출 통계");
    	System.out.println("3. 일별 매출 통계");
    	System.out.println("4. 나가기");
    	System.out.println("---------------------------------");
    	System.out.print("번호를 입력하세요 >> ");
    }
    
}
