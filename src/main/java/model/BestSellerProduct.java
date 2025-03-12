package model;

import java.util.*;

public class BestSellerProduct {
	
	
	List<Map.Entry<Product, Integer>> rank; // 주문 목록에서 많이 팔린 상품 순으로 나열한 상품 목록
	
	public BestSellerProduct() {
		this.rank = new ArrayList<>();
	}
	
	// 가장 많이 팔린 상품 가져오기
	public Map.Entry<Product, Integer> getBestProduct() {
		return this.rank.get(0);
	}
	
	// 가장 적게 팔린 상품 가져오기
	public Map.Entry<Product, Integer> getWorstProduct() {
		return this.rank.get(rank.size()-1);
	}
	
	// 주문 목록에서 많이 팔린 상품 순으로 나열한 상품 목록 가져오기 
	public List<Map.Entry<Product, Integer>> getProducts(){
		return this.rank;
	}
	
	public void setProducts(List<Map.Entry<Product, Integer>> list) {
		this.rank = list;
	}
	
}
