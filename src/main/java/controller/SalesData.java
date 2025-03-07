package controller;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import manager.ProductManager;
import model.Product;

public class SalesData {
	//날짜, Product 아이디, 수량
	private Map<LocalDate, Map<Product, Integer>> transactionMap;//연도, 음식id, 수량
	private ProductManager productManager;
	private List<Product> prodcutList;
	
	public SalesData() {
		this.transactionMap = new HashMap <LocalDate, Map<Product, Integer>>();
		this.productManager = new ProductManager();
		this.prodcutList = productManager.getAllProducts();
	}
	
	//년도별 조회
	public void viewMonthlyReport() {
	
		
	}
	public void getMonthlyReport() {
		
		
	}
	//월별 조회
	public void getDailyReport() {
		
	}
	//일별 조회
	public void getAnnualReport() {
		
	}
	

	
	
}