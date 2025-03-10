package manager;

import model.Coffee;
import model.Product;

import java.util.*;

public class ProductManager {
    private List<Product> products;
    private Scanner scanner;
    private int nextProductId;

    public ProductManager() {
        this.products = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.nextProductId = 0;

    }

    // 테스트용으로 미리 값 박아둔것
    private void testInit() {
        products.add(new Coffee(1, "아메리카노", 2500));
        products.add(new Coffee(2, "카페라떼", 2500));
        products.add(new Coffee(3, "콜드브루", 3000));
        products.add(new Coffee(4, "카푸치노", 3000));
        products.add(new Coffee(5, "카페모카", 3000));
        products.add(new Coffee(6, "바닐라라떼", 4000));
        products.add(new Coffee(7, "아인슈페너", 3000));
    }

    // 상품 목록 조회
    // 관리자가 추가, 수정한 상품 목록들을 반환
    // 사용하는 클래스
    // 관리자가 등록한 메뉴들만 손님이 주문할 수 있어야 한다
    public List<Product> getAllProducts() {
        return new ArrayList<>(products); // 원본 보호
    }
    
    // 상품 추가
	public void addProduct() {

		int nextProductId = 0;
		int price = 0;
		try {
			nextProductId = Integer.parseInt(scanner.nextLine());	//ID는 숫자형으로 입력받기
		} catch (NumberFormatException e) {
			System.out.println("잘못된 입력입니다. ID는 숫자로 입력해주세요.");
			return;
		}
		String name = scanner.nextLine();	//이름은 String 혹은 숫자형으로 혼합해서 받을 수 있음
		try {
			price = Integer.parseInt(scanner.nextLine());	//가격은 숫자형으로 입력받기
		} catch (NumberFormatException e) {
			System.out.println("잘못된 입력입니다. 가격은 숫자로 입력해주세요.");
			return;
		}
		products.add(new Coffee(nextProductId, name, price));
		
	}
	
	// 상품 변경
	public void changeProduct() {
		
		int id = 0;
		int price = 0;
		try {
			System.out.println("상품 ID 입력: ");
			id = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("잘못된 입력입니다. ID는 숫자로 입력해주세요.");
			return;
		}

		// 입력된 ID가 products에 있는지 확인
		List<Integer> idList = getAllProductIds();
		boolean isIdExist = idList.contains(id);
		if (!isIdExist) {
			System.out.println("해당 상품 ID가 리스트에 없습니다");
			return;
		} else {
			System.out.println("이름을 변경하시리면 1번, 가격을 변경하시려면 2번을 선택해주세요");
			int selectNum = Integer.parseInt(scanner.nextLine());
			if (selectNum==1) {
				System.out.println("변경하실 이름을 입력해주세요");
				String name = scanner.nextLine();
				products.get(id-1).setName(name);	// 인덱스 기준으로 삭제되기 때문에 실제ID에서 1뺐습니다
			} else {
				try {
					System.out.println("변경하실 가격을 입력해주세요");
					price = Integer.parseInt(scanner.nextLine());
				} catch (Exception e) {
					System.out.println("잘못된 입력입니다. 가격은 숫자로 입력해주세요.");
					return;
				}
				products.get(id-1).setPrice(price);
			}
		}
		System.out.println("변경했습니다");	
	}

	// 상품 삭제
	public void removeProduct() {
		
		System.out.println("상품 ID 입력: ");
		int id = Integer.parseInt(scanner.nextLine());
		
		// 입력된 ID가 products에 있는지 확인
		List<Integer> idList = getAllProductIds();
		boolean isIdExist = idList.contains(id);
		if (!isIdExist) {
			System.out.println("해당 상품 ID가 리스트에 없습니다");
			return;
		} else {
			System.out.println("삭제하시려면 1번, 그대로 유지하시려면 2번을 선택해주세요");
			int selectNum = Integer.parseInt(scanner.nextLine());
			if (selectNum==1) {
				products.remove(id-1);	// 인덱스 기준으로 삭제되기 때문에 실제ID에서 1뺐습니다
			} else {
			}
		}
		System.out.println("변경했습니다");
	}
    
    public List<Integer> getAllProductIds(){
    	ArrayList<Integer> results = new ArrayList<>();
    	for (int i = 0; i < products.size(); i++) {
    		results.add(products.get(i).getProductId());
    	}
    	return results;
    }
    
	public void showProducts() {
        System.out.println("=======================================");
		System.out.println("상품 관리 화면입니다");	// 우선 ProductManager 출력화면을 "상품 관리 화면"으로 설정했습니다
        System.out.println("1. 상품 추가, 2. 상품 변경, 3. 상품 삭제, 4. 상품 리스트 조회");
        System.out.println("=======================================");
	}

	public void run() {
		
		//testInit();

		while(true) {
			showProducts();
			String caseNum = scanner.nextLine();
			switch (caseNum) {
				case "1":
					System.out.println("상품 추가합니다");
					addProduct();
					break;
				case "2":
					System.out.println("상품 변경합니다");
					changeProduct();
					break;
				case "3":
					System.out.println("상품 삭제합니다");
					removeProduct();
					break;
				case "4":
					System.out.println("모든 상품 리스트를 조회합니다");
					List<Product> products = getAllProducts();
					for (Product product : products) {
						System.out.println(product);
					}
					break;
				default:
			}
		}
		
	}
  
}