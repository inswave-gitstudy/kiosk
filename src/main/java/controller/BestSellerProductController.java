package controller;

import manager.BestSellerProductManager;
import java.util.*;
import model.Product;

public class BestSellerProductController {
	
	BestSellerProductManager bestSellerProductManager;
	
	public BestSellerProductController(OrderController orderController) {
		this.bestSellerProductManager = new BestSellerProductManager(orderController);
		this.bestSellerProductManager.rank();
	}
	
	// 전체 판매 순위(랭크) 가져오기
	public List<Map.Entry<Product, Integer>> getRank(){
		return bestSellerProductManager.getRank();
	}
	
	// 가장 많이 팔린 상품 가져오기
	public Map.Entry<Product, Integer> getBestProduct() {
		return this.bestSellerProductManager.getBestProduct();
	}
	
	// 가장 적게 팔린 상품 가져오기
	public Map.Entry<Product, Integer> getWorstProduct() {
		return this.bestSellerProductManager.getWorstProduct();
	}
	
	// 전체 판매 순위(랭크) 보여주기
	public void showRank() {
		if(this.bestSellerProductManager.getRank() == null || this.bestSellerProductManager.getRank().isEmpty()) {
			System.out.println("주문 완료된 주문 목록이 없습니다..!");
			return;
		}
		System.out.println("========= 전체 판매 순위 =========");
		for(Map.Entry<Product, Integer> entry : this.bestSellerProductManager.getRank()) {
			System.out.println("[상품 번호:" + entry.getKey().getProductId() +
							    "] [상품명:" + entry.getKey().getName() +
							    "] [총 판매 수량:"+ entry.getValue() + "]");
		}
		System.out.println("==============================");
	}
	
	// 가장 많이 팔린 상품 보여주기
	public void showBestProduct() {
		if(this.bestSellerProductManager.getRank() == null || this.bestSellerProductManager.getRank().isEmpty()) {
			System.out.println("주문 완료된 주문 목록이 없습니다..!");
			return;
		}
		System.out.print("가장 많이 팔린 상품 : ");
		System.out.println("[상품 번호:" + this.bestSellerProductManager.getBestProduct().getKey().getProductId() +
			    "] [상품명:" + this.bestSellerProductManager.getBestProduct().getKey().getName() +
			    "] [총 판매 수량:"+ this.bestSellerProductManager.getBestProduct().getValue() + "]");
		
	}
	
	// 가장 적게 팔린 상품 보여주기
	public void showWorstProduct() {
		if(this.bestSellerProductManager.getRank() == null || this.bestSellerProductManager.getRank().isEmpty()) {
			System.out.println("주문 완료된 주문 목록이 없습니다..!");
			return;
		}
		System.out.print("가장 적게 팔린 상품 : ");
		System.out.println("[상품 번호:" + this.bestSellerProductManager.getWorstProduct().getKey().getProductId() +
			    "] [상품명:" + this.bestSellerProductManager.getWorstProduct().getKey().getName() +
			    "] [총 판매 수량:"+ this.bestSellerProductManager.getWorstProduct().getValue() + "]");
	}
	
}
