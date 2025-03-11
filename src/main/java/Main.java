import controller.MainController;
import manager.ProductManager;

public class Main {
    public static void main(String[] args) {
        //MainController mainController = new MainController();
        //mainController.start();
    	ProductManager pm1 = new ProductManager();
    	pm1.run();
    }
}