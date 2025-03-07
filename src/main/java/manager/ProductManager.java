package manager;

import model.Coffee;
import model.Product;

import java.util.*;

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
        products.add(new Coffee(4L,"카푸치노",3000));
        products.add(new Coffee(5L,"카페모카",3000));
        products.add(new Coffee(6L,"바닐라라떼",4000));
        products.add(new Coffee(7L,"아인슈페너",3000));

    }

    // 상품 목록 조회
    // 관리자가 추가, 수정한 상품 목록들을 반환
    // 사용하는 클래스
    // 관리자가 등록한 메뉴들만 손님이 주문할 수 있어야 한다
    public List<Product> getAllProducts() {
        return new ArrayList<>(products); // 원본 보호
    }


}
