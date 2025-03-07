package manager;

import java.util.List;
import java.util.Scanner;

import model.Coffee;
import model.Dessert;
import model.Product;

public class ProductSelector {
    private ProductManager productManager;
    private Scanner scanner;

    public ProductSelector(ProductManager productManager) {
        this.productManager = productManager;
        this.scanner = new Scanner(System.in);
    }

    public Product selectProduct() {
        System.out.println("===== 상품 선택 =====");
        List<Product> products = productManager.getAllProducts();

        for (int i = 0; i < products.size(); i++) {
            System.out.println((i + 1) + ". " + products.get(i));
        }

        System.out.print("상품 번호 입력: ");
        int productNumber = Integer.parseInt(scanner.nextLine()) - 1;

        Product selectedProduct = products.get(productNumber);

        // 커피 상품일 경우
        if (selectedProduct instanceof Coffee) {
            Coffee coffee = (Coffee) selectedProduct;
            System.out.print("디카페인 여부 (true/false): ");
            coffee.setDecaf(Boolean.parseBoolean(scanner.nextLine()));

            System.out.print("원두 종류 (에티오페아/콜롬비아): ");
            coffee.setBeanType(scanner.nextLine());

            System.out.print("아이스 여부 (true/false): ");
            coffee.setIced(Boolean.parseBoolean(scanner.nextLine()));

            System.out.println("선택된 상품: " + coffee.showOption());
            return coffee;

        // 디저트 상품일 경우
        } else if (selectedProduct instanceof Dessert) {
            Dessert dessert = (Dessert) selectedProduct;
            System.out.print("조각케이크인가요? (true/false): ");
            dessert.setSlice(Boolean.parseBoolean(scanner.nextLine()));

            System.out.println("선택된 상품: " + dessert.showOption());
            return dessert;
        }

        return null;
    }
}
