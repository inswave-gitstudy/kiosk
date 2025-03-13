package manager;

import java.util.*;

import controller.OrderController;
import model.BestSellerProduct;
import model.Order;
import model.Product;
import repository.OrderRepository;
import repository.TxtOrderRepository;

// loadOrder()


public class BestSellerProductManager{
	
	BestSellerProduct bestSellerProduct;
	OrderController orderController;
	
	public BestSellerProductManager(OrderController orderController) {
		this.bestSellerProduct = new BestSellerProduct();
		this.orderController = orderController;
	}
	
	// txt 파일을 불러와서 정보를 읽고 , 판매량순으로 정렬하고 bestSellerProduct 의 products에 set..
	public void rank(){
		
		Map<Integer, Order> orders = new TreeMap<>();
		
		// 주문 완료 처된 order.txt 파일에서 주문 목록을 불러옴.
		try {
			orders = orderController.loadOrderWithTxt();
			if(orders.isEmpty()) {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		// 각 상품의 판매량을 담을 Map
		Map<Product, Integer> eachProductSales = new HashMap<>();

		
		for(Order order : orders.values()) {
			
			for(Map.Entry<Product, Integer> entry : order.getProducts().entrySet()) {
				Product product = entry.getKey();
				int quantity = entry.getValue();
				
				eachProductSales.put(product, eachProductSales.getOrDefault(product, 0) + quantity);
			}
		}
		
		// 많이 팔린 상품순으로 나열..
		List<Map.Entry<Product, Integer>> list = new ArrayList<>(eachProductSales.entrySet());
		list.sort((o1, o2) -> o2.getValue() - o1.getValue());
		
		this.bestSellerProduct.setProducts(list);
	}
	
	public Map.Entry<Product, Integer> getBestProduct() {
		return this.bestSellerProduct.getBestProduct();
	}
	
	public Map.Entry<Product, Integer> getWorstProduct() {
		return this.bestSellerProduct.getWorstProduct();
	}
	
	public List<Map.Entry<Product, Integer>> getRank(){
		return this.bestSellerProduct.getProducts();
	}
	
	
}


