import manager.OrderManager;
import model.Coffee;
import model.Order;
import model.Product;
import repository.OrderRepository;
import repository.TxtOrderRepository;

import java.util.HashMap;
import java.util.Map;

public class UpdateOrderStatusTest {
    public static void main(String[] args) {
        OrderRepository storage = new TxtOrderRepository();
        OrderManager orderManager = new OrderManager(storage);
        Map<Product, Integer> cart = new HashMap<>();
        cart.put(new Coffee(1L,"아메리카노",2000),2);
        cart.put(new Coffee(2L, "카페라떼", 2500),1);
        cart.put(new Coffee(3L, "콜드브루", 3000),3);
        cart.put(new Coffee(4L, "카푸치노", 3000),4);
        cart.put(new Coffee(5L, "카페모카", 3000),5);
        cart.put(new Coffee(6L, "바닐라라떼", 4000),6);
        Order order = orderManager.createOrder(cart);
        System.out.println(order);
        System.out.println("주문 상태 변경 : 준비중 -> 완료");
        orderManager.completeOrder(1);
        System.out.println(order);
        orderManager.cancelOrder(1);
        System.out.println(order);
    }
}
