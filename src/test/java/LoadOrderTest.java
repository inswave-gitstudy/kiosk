import manager.OrderManager;
import repository.ObjectOrderRepository;
import repository.OrderRepository;

public class LoadOrderTest {
    public static void main(String[] args) {
        OrderRepository storage = new ObjectOrderRepository();
        OrderManager orderManager = new OrderManager(storage);
        orderManager.loadOrder();
        orderManager.getAllOrder();
    }
}
