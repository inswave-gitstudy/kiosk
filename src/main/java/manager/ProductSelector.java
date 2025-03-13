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

    private static final List<String> BEAN_TYPES = Arrays.asList("에티오피아", "브라질", "콜롬비아");

    public ProductSelector(ProductManager productManager) {
        this.productManager = productManager;
        this.scanner = new Scanner(System.in);
    }

    public Product selectProduct() {
        while (true) {
            System.out.println("===== 상품 선택 =====");
            System.out.println("0. 이전 화면으로 돌아가기");
            System.out.println("1. 커피");
            System.out.println("2. 디저트");
            int productTypeChoice = getValidNumberInput("상품 종류를 선택하세요: ", 0, 2);

            if (productTypeChoice == 0) {
                return null;  // 이 부분은 이전 화면으로 돌아가도록 종료하는 처리입니다.
            }

            if (productTypeChoice == 1) {
                Product coffee = selectCoffee();
                if (coffee != null) {
                    return coffee;
                }
            } else if (productTypeChoice == 2) {
                Product dessert = selectDessert();
                if (dessert != null) {
                    return dessert;
                }
            } else {
                System.out.println("잘못된 선택입니다. 다시 시도해주세요.");
            }
        }
    }

    private Product selectCoffee() {
        while (true) {
            System.out.println("===== 커피 선택 =====");
            List<Product> products = productManager.getAllProducts();

            System.out.println("0. 상품 선택 화면으로 돌아가기"); // 상품 선택 화면으로 돌아가는 0번 옵션
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i) instanceof Coffee) {
                    System.out.println((i + 1) + ". " + products.get(i));
                }
            }

            int productNumber = getValidNumberInput("커피 번호 입력: ", 0, products.size()) - 1;
            if (productNumber == -1) { 
                // 0을 누르면 커피 선택 화면을 벗어날 수 있도록 하여 상품 선택 화면으로 돌아가게 됨
                break;
            }

            Product selectedProduct = products.get(productNumber);
            Coffee originalCoffee = (Coffee) selectedProduct;
            Coffee coffee = new Coffee(originalCoffee.getProductId(), originalCoffee.getName(), originalCoffee.getPrice());

            System.out.println("카페인 여부를 선택하세요:");
            System.out.println("0. 커피 선택으로 돌아가기");
            System.out.println("1. 카페인");
            System.out.println("2. 디카페인");
            int decafChoice = getValidNumberInput("번호 입력: ", 0, 2);
            if (decafChoice == 0) continue; // 0 입력 시 커피 선택으로 돌아가기
            coffee.setDecaf(decafChoice == 2);

            System.out.println("원두를 선택하세요:");
            System.out.println("0. 커피 선택으로 돌아가기");
            for (int i = 0; i < BEAN_TYPES.size(); i++) {
                System.out.println((i + 1) + ". " + BEAN_TYPES.get(i));
            }
            int beanChoice = getValidNumberInput("번호 입력: ", 0, BEAN_TYPES.size());
            if (beanChoice == 0) continue; // 0 입력 시 커피 선택으로 돌아가기
            coffee.setBeanType(BEAN_TYPES.get(beanChoice - 1));

            System.out.println("아이스 여부를 선택하세요:");
            System.out.println("0. 커피 선택으로 돌아가기");
            System.out.println("1. 핫");
            System.out.println("2. 아이스");
            int icedChoice = getValidNumberInput("번호 입력: ", 0, 2);
            if (icedChoice == 0) continue; // 0 입력 시 커피 선택으로 돌아가기
            coffee.setIced(icedChoice == 2);

            System.out.println("선택된 커피: " + coffee.showOption());
            return coffee;
        }

        return null;  // 커피 선택 화면을 벗어나서 null 반환
    }

    private Product selectDessert() {
        while (true) {
            System.out.println("===== 디저트 선택 =====");
            List<Product> products = productManager.getAllProducts();

            System.out.println("0. 상품 선택 화면으로 돌아가기"); // 상품 선택 화면으로 돌아가는 0번 옵션
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i) instanceof Dessert) {
                    System.out.println((i + 1) + ". " + products.get(i));
                }
            }

            int productNumber = getValidNumberInput("디저트 번호 입력: ", 0, products.size()) - 1;
            if (productNumber == -1) { 
                // 0을 누르면 디저트 선택 화면을 벗어날 수 있도록 하여 상품 선택 화면으로 돌아가게 됨
                break;
            }

            Product selectedProduct = products.get(productNumber);
            Dessert originalDessert = (Dessert) selectedProduct;
            Dessert dessert = new Dessert(originalDessert.getProductId(), originalDessert.getName(), originalDessert.getPrice());

            System.out.println("케이크 종류를 선택하세요:");
            System.out.println("0. 디저트 선택으로 돌아가기");
            System.out.println("1. 조각 케이크");
            System.out.println("2. 홀 케이크");
            int sliceChoice = getValidNumberInput("번호 입력: ", 0, 2);
            if (sliceChoice == 0) continue; // 0 입력 시 디저트 선택으로 돌아가기
            dessert.setSlice(sliceChoice == 1);

            System.out.println("선택된 디저트: " + dessert.showOption());
            return dessert;
        }

        return null;  // 디저트 선택 화면을 벗어나서 null 반환
    }

    private int getValidNumberInput(String message, int min, int max) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();
            try {
                int number = Integer.parseInt(input);
                if (number >= min && number <= max) {
                    return number; 
                } else {
                    System.out.println("잘못된 입력입니다. 알맞은 숫자를 입력해주세요.");
                }
            } catch (NumberFormatException e) {
                System.out.println("잘못된 입력입니다. 숫자를 입력해 주세요.");
            }
        }
    }
}
