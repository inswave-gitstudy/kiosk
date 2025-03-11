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

        int productNumber = getValidNumberInput("상품 번호 입력: ", 1, products.size()) - 1;
        Product selectedProduct = products.get(productNumber);

        // 커피 상품일 경우 - 새로운 객체 생성 후 옵션 설정
        if (selectedProduct instanceof Coffee) {
            Coffee originalCoffee = (Coffee) selectedProduct;
            Coffee coffee = new Coffee(originalCoffee.getProductId(), originalCoffee.getName(), originalCoffee.getPrice());

            // 디카페인 여부 선택
            System.out.println("디카페인 여부를 선택하세요:");
            System.out.println("1. 카페인  2. 디카페인");
            int decafChoice = getValidNumberInput("번호 입력: ", 1, 2);
            coffee.setDecaf(decafChoice == 2);

            // 원두 선택
            System.out.println("원두를 선택하세요:");
            for (int i = 0; i < BEAN_TYPES.size(); i++) {
                System.out.println((i + 1) + ". " + BEAN_TYPES.get(i));
            }
            int beanChoice = getValidNumberInput("번호 입력: ", 1, BEAN_TYPES.size());
            coffee.setBeanType(BEAN_TYPES.get(beanChoice - 1));

            // 아이스 여부 선택
            System.out.println("아이스 여부를 선택하세요:");
            System.out.println("1. 핫  2. 아이스");
            int icedChoice = getValidNumberInput("번호 입력: ", 1, 2);
            coffee.setIced(icedChoice == 2);

            System.out.println("선택된 상품: " + coffee.showOption());
            return coffee;

        // 디저트 상품일 경우 - 새로운 객체 생성 후 옵션 설정
        } else if (selectedProduct instanceof Dessert) {
            Dessert originalDessert = (Dessert) selectedProduct;
            Dessert dessert = new Dessert(originalDessert.getProductId(), originalDessert.getName(), originalDessert.getPrice());

            // 케이크 조각 여부 선택
            System.out.println("케이크 종류를 선택하세요:");
            System.out.println("1. 조각 케이크  2. 홀 케이크");
            int sliceChoice = getValidNumberInput("번호 입력: ", 1, 2);
            dessert.setSlice(sliceChoice == 1);

            System.out.println("선택된 상품: " + dessert.showOption());
            return dessert;
        }

        return null;
    }

    // 유효한 숫자 입력을 받을 때까지 반복하는 메서드
    private int getValidNumberInput(String message, int min, int max) {
        while (true) {  // 무한 루프 시작
            System.out.print(message);  // 사용자에게 입력을 요청하는 메시지 출력
            String input = scanner.nextLine();  // 사용자로부터 문자열 입력 받기

            try {
                // 입력 받은 문자열을 정수로 변환 시도
                int number = Integer.parseInt(input);
                
                // 숫자가 min과 max 사이에 있는지 체크
                if (number >= min && number <= max) {
                    return number;  // 조건을 만족하면 숫자 반환
                } else {
                    System.out.println("잘못된 입력입니다. 알맞은 숫자를 입력해주세요.");  // 범위 밖의 숫자 입력 시 메시지 출력
                }
            } catch (NumberFormatException e) {
                // 입력 받은 값이 숫자가 아닐 경우 예외 처리
                System.out.println("잘못된 입력입니다. 숫자를 입력해 주세요.");  // 숫자가 아닌 값을 입력했을 때 메시지 출력
            }
        }
    }

}