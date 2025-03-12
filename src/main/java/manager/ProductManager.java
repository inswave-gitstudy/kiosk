package manager;

import model.*;
import java.util.*;
import java.util.regex.*;


public class ProductManager {
    private List<Product> products;
    private Scanner scanner;
    private int nextProductId;

    public ProductManager() {
        this.products = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.nextProductId = 1;
		testInit();
    }

    // 테스트용으로 미리 값 박아둔것
    private void testInit() {
		addProduct("아메리카노", 2500);
		addProduct("카페라떼", 2500);
		addProduct("콜드브루", 3000);
		addProduct("카푸치노", 3000);
		addProduct("카페모카", 3000);
		addProduct("바닐라라떼", 2500);
		addProduct("아인슈페너",3000);
    }

	private void addProduct(String name, int price) {
		products.add(new Coffee(nextProductId, name, price));
		nextProductId++;
	}

    //상품 추가
	public void addProduct() {

		int price = 0;

		System.out.println("이름 입력해주세요: ");
		String name = scanner.nextLine();	//이름은 문자열 혹은 숫자형으로 혼합해서 입력
		boolean isNoSpecialChar = removeSpecialCharacters(name);	//이름 특수문자 여부 확인
		if (!isNoSpecialChar) {
			System.out.println("특수문자 입력은 불가능합니다. 다시 입력해주세요.");
			return;
		}
		List<String> nameList = getAllProductNames();
		boolean isNameExist = nameList.contains(name);	//이름 중복 확인
		if(isNameExist) {
			System.out.println("중복된 이름입니다. 다시 입력해주세요.");
			return;
		}
		try {
			System.out.println("가격 입력해주세요: ");
			price = Integer.parseInt(scanner.nextLine());	//가격은 숫자형으로 입력받기
		} catch (NumberFormatException e) {
			System.out.println("잘못된 입력입니다. 가격은 숫자로 입력해주세요.");
			return;
		}
		products.add(new Coffee(nextProductId, name, price));
		nextProductId++;	
	}
	
	//상품 삭제
	public void removeProduct() {
		
		System.out.println("상품 ID 입력: ");
		int id = Integer.parseInt(scanner.nextLine());
		
		//입력된 상품 ID가 상품 리스트에 있는지 확인
		Product productToRemove = null;
		for (Product product : products) {
			if (product.getProductId() == id) {
				productToRemove = product;
				break;
			}
		}
		if(productToRemove == null) {
			System.out.println("해당 상품 ID가 리스트에 없습니다");
			return;
		} else {
			System.out.println("삭제하시려면 1번, 그대로 유지하시려면 2번을 선택해주세요.");
			int selectNum = Integer.parseInt(scanner.nextLine());
			if (selectNum==1) {
				products.remove(productToRemove);
			} else {
			}
		}
		System.out.println("변경했습니다.");
	}
	
    //상품 리스트 조회
    public List<Product> getAllProducts() {
        return new ArrayList<>(products); // 원본 보호
    }
	
	//상품 수정
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

		//입력된 상품 ID가 상품 리스트에 있는지 확인
		Product productToChange = null;
		for (Product product : products) {
			if (product.getProductId() == id) {
				productToChange = product;
				break;
			}
		}		
		if(productToChange == null) {
			System.out.println("해당 상품 ID가 리스트에 없습니다");
			return;
		} else {
			System.out.println("이름 변경은 1번, 가격 변경은 2번을 선택해주세요.");
			int selectNum = Integer.parseInt(scanner.nextLine());
			if (selectNum==1) {
				System.out.println("변경하실 이름을 입력해주세요: ");
				String name = scanner.nextLine();
				boolean isNoSpecialChar = removeSpecialCharacters(name);	//이름 특수문자 여부 확인
				if (!isNoSpecialChar) {
					System.out.println("특수문자 입력은 불가능합니다. 다시 입력해주세요.");
					return;
				}
				List<String> nameList = getAllProductNames();
				boolean isNameExist = nameList.contains(name);	//이름 중복 확인
				if(isNameExist) {
					System.out.println("중복된 이름입니다. 다시 입력해주세요.");
					return;
				}
				productToChange.setName(name);	//이름 수정 
			} else {
				try {
					System.out.println("변경하실 가격을 입력해주세요: ");
					price = Integer.parseInt(scanner.nextLine());
				} catch (Exception e) {
					System.out.println("잘못된 입력입니다. 가격은 숫자로 입력해주세요.");
					return;
				}
				productToChange.setPrice(price);
			}
		}
		System.out.println("변경했습니다");	
	}
    
    //상품명 리스트 생성
    public List<String> getAllProductNames(){
    	ArrayList<String> results = new ArrayList<>();
    	for (int i = 0; i < products.size(); i++) {
    		results.add(products.get(i).getName());
    	}
    	return results;
    }
    
    //상품관리 메뉴
	public void showProducts() {
        System.out.println("------------상품 관리 화면-----------");
        System.out.println("1. 상품추가");
        System.out.println("2. 상품삭제");
        System.out.println("3. 상품조회");
        System.out.println("4. 상품수정");
        System.out.println("5. 나가기");
        System.out.println("---------------------------------");
        System.out.print("번호를 입력하세요 >> ");
	}

	//상품명에 특수문자가 없는지 확인
	public boolean removeSpecialCharacters(String str) {
		return Pattern.matches("[가-힣a-zA-Z0-9]+", str);
	}
	
	public void run() {

		//testInit();

		while(true) {
			showProducts();
			String caseNum = scanner.nextLine();
			switch (caseNum) {
				case "1":
					System.out.println("상품 추가합니다.");
					addProduct();
					break;
				case "2":
					System.out.println("상품 삭제합니다.");
					removeProduct();
					break;
				case "3":
					System.out.println("모든 상품 리스트를 조회합니다.");
					List<Product> products = getAllProducts();
					for (Product product : products) {
						System.out.println(product);
					}
					break;
				case "4":
					System.out.println("상품 수정합니다.");
					changeProduct();
					break;
				case "5":
					System.out.println("메인 메뉴로 돌아갑니다.");
					return;
				default:
			}
		}
	
	}
}