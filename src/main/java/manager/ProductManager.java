package manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Coffee;
import model.Product;

public class ProductManager {
    private List<Product> products;
    private Scanner scanner;
    private long nextProductId;

    public ProductManager() {
        this.products = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.nextProductId = 1;
        testInit();
    }

    // 테스트용으로 미리 값 박아둔것
    private void testInit() {
        products.add(new Coffee(1L,"아메리카노",2500));
        products.add(new Coffee(2L,"카페라떼",2500));
        products.add(new Coffee(3L,"콜드브루",3000));
    }

    // 상품 목록 조회
    // 관리자가 추가, 수정한 상품 목록들을 반환
    // 사용하는 클래스
    // 관리자가 등록한 메뉴들만 손님이 주문할 수 있어야 한다
    public List<Product> getAllProducts() {
        return new ArrayList<>(products); // 원본 보호
    }

    // 상품 추가 메서드
    public void addProduct(Product product) {
        products.add(product);
    }
}
