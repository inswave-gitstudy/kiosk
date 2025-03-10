package manager;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import model.Coffee;
import model.Dessert;
import model.Product;

public class ProductSelector {
    private ProductManager productManager;
    private Scanner scanner;

    // 원두 목록 (사용자는 여기서만 선택 가능, 추가/삭제 x)
    private static final List<String> BEAN_TYPES = Arrays.asList("에티오피아", "브라질", "콜롬비아");

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

        // 커피 상품일 경우 - 새로운 객체 생성 후 옵션 설정
        if (selectedProduct instanceof Coffee) {
            Coffee originalCoffee = (Coffee) selectedProduct;

            // 새로운 Coffee 객체 생성 (기존 객체 수정하지 않음)
            Coffee coffee = new Coffee(originalCoffee.getProductId(), originalCoffee.getName(), originalCoffee.getPrice());

            System.out.print("디카페인 여부 (true/false): ");
            coffee.setDecaf(Boolean.parseBoolean(scanner.nextLine()));

            // 원두 선택 (번호 입력 방식)
            System.out.println("원두를 선택하세요:");
            for (int i = 0; i < BEAN_TYPES.size(); i++) {
                System.out.println((i + 1) + ". " + BEAN_TYPES.get(i));
            }
            int beanChoice;
            while (true) {
                System.out.print("번호 입력: ");
                beanChoice = Integer.parseInt(scanner.nextLine());
                if (beanChoice >= 1 && beanChoice <= BEAN_TYPES.size()) {
                    break;
                }
                System.out.println("잘못된 입력입니다. 다시 선택하세요.");
            }
            coffee.setBeanType(BEAN_TYPES.get(beanChoice - 1));  // 선택한 원두 적용

            System.out.print("아이스 여부 (true/false): ");
            coffee.setIced(Boolean.parseBoolean(scanner.nextLine()));

            System.out.println("선택된 상품: " + coffee.showOption());
            return coffee;  // 새롭게 생성된 객체 반환

        // 디저트 상품일 경우 - 새로운 객체 생성 후 옵션 설정
        } else if (selectedProduct instanceof Dessert) {
            Dessert originalDessert = (Dessert) selectedProduct;

            // 새로운 Dessert 객체 생성 (기존 객체 수정하지 않음)
            Dessert dessert = new Dessert(originalDessert.getProductId(), originalDessert.getName(), originalDessert.getPrice());

            System.out.print("조각케이크인가요? (true/false): ");
            dessert.setSlice(Boolean.parseBoolean(scanner.nextLine()));

            System.out.println("선택된 상품: " + dessert.showOption());
            return dessert;  // 새롭게 생성된 객체 반환
        }

        return null;
    }
}
