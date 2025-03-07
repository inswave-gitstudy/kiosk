import manager.OrderManager;
import model.Coffee;
import model.Order;
import model.Product;
import repository.ObjectOrderRepository;
import repository.OrderRepository;

import java.util.LinkedHashMap;
import java.util.Map;

public class SaveOrderTest {
    public static void main(String[] args) {
        OrderRepository storage = new ObjectOrderRepository();
        OrderManager orderManager = new OrderManager(storage);
        Map<Product, Integer> cart = new LinkedHashMap<>();
        cart.put(new Coffee(1L,"아메리카노",2000),2);
        cart.put(new Coffee(2L, "카페라떼", 2500),1);
        cart.put(new Coffee(3L, "콜드브루", 3000),3);
        cart.put(new Coffee(4L, "카푸치노", 3000),4);
        cart.put(new Coffee(5L, "카페모카", 3000),5);
        cart.put(new Coffee(6L, "바닐라라떼", 4000),6);
        Order order = orderManager.createOrder(cart);
        System.out.println(order);
        orderManager.flushOrder();

    }
}
