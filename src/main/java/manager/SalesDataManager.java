package manager;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Map.Entry;

import model.Product;

public class SalesDataManager {
	//파일 이름 리턴하는 기능
	private String fileTitleFormat() {
		LocalDateTime now = LocalDateTime.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		return "C:\\Temp\\키오스크_통계파일_" + formatter.format(now) + ".txt";
	}
	
	//연도별 파일 저장기능
	public void saveYearSalesReportToFile(Map<Integer, Map<Product, Integer>> yearlySalesMap) {
	    
	    String fileName = fileTitleFormat();

	    try{
	    	PrintWriter pw = new PrintWriter(fileName);
	        pw.println("========== 키오스크 연도별 판매 통계 ==========");
	        
	        for (Map.Entry<Integer, Map<Product, Integer>> entry : yearlySalesMap.entrySet()) {
	        	Map<Product, Integer> map = entry.getValue();
				int year = entry.getKey();

	            pw.println("[" + year + "년]");
	            pw.println("-----------------------------------------------------");
	            pw.println("[상품명]\t\t[가격]\t\t[수량]\t\t[매출]");
	            pw.println("-----------------------------------------------------");

	            int totalSalesCount = 0;
	            int totalSales = 0;

	            for (Map.Entry<Product, Integer> value : map.entrySet()) {
	            	Product name = value.getKey(); //음식명
	                int count = value.getValue();  //판매개수
	                int price = name.getPrice();   //가격
	                int sales = price * count;     //매출

	                pw.printf("%-10s\t%,6d원\t%,6d개\t%,10d원\n", 
	                          name.getName(), price, count, sales);

	                totalSalesCount += count;
	                totalSales += sales;
	            }

	            pw.println("-----------------------------------------------------");
	            pw.printf("총 판매 개수: %d개\n", totalSalesCount);
	            pw.printf("총 매출: %d원\n\n", totalSales);
	        }

	        pw.close();
	        System.out.println("연도별 통계 파일이 저장되었습니다>> " + fileName);
	    } catch (Exception e) {
	        System.out.println("파일 저장 중 오류 발생: " + e.getMessage());
	    }
	}
	
	//월별 파일 저장기능
	public void saveMonthlySalesReportToFile(Map<Integer, Map<Integer, Map<Product, Integer>>> monthlySalesMap) {
	    String fileName = fileTitleFormat();

	    try (PrintWriter pw = new PrintWriter(fileName)) {
	        pw.println("========== 키오스크 월별 판매 통계 ==========");

	        for (Map.Entry<Integer, Map<Integer, Map<Product, Integer>>> yearEntry : monthlySalesMap.entrySet()) {
	            int year = yearEntry.getKey();
	            Map<Integer, Map<Product, Integer>> months = yearEntry.getValue();

	            pw.println("[" + year + "년]");
	            
	            for (Map.Entry<Integer, Map<Product, Integer>> monthEntry : months.entrySet()) {
	                int month = monthEntry.getKey();
	                Map<Product, Integer> products = monthEntry.getValue();

	                pw.println("[" + month + "월]");
	                pw.println("-----------------------------------------------------");
	                pw.println("[상품명]\t\t[가격]\t\t[수량]\t\t[매출]");
	                pw.println("-----------------------------------------------------");

	                int totalSalesCount = 0;
	                int totalRevenue = 0;

	                for (Map.Entry<Product, Integer> productEntry : products.entrySet()) {
	                    Product product = productEntry.getKey();
	                    int quantity = productEntry.getValue();
	                    int price = product.getPrice();
	                    int revenue = price * quantity;

	                    pw.printf("%-10s\t%,6d원\t%,6d개\t%,10d원\n",
	                              product.getName(), price, quantity, revenue);

	                    totalSalesCount += quantity;
	                    totalRevenue += revenue;
	                }

	                pw.println("-----------------------------------------------------");
	                pw.printf("총 판매 개수: %,d개\n", totalSalesCount);
	                pw.printf("총 매출: %,d원\n\n", totalRevenue);
	            }
	        }

	        pw.close();
	        System.out.println("월별 통계 파일이 저장되었습니다: " + fileName);
	    } catch (Exception e) {
	        System.out.println("파일 저장 중 오류 발생: " + e.getMessage());
	    }
	}
	
	//일별 파일 저장기능
	public void saveDailySalesReportToFile(Map<LocalDate, Map<Product, Integer>> transactionMap) {
	    String fileName = fileTitleFormat();

	    try {
	    	PrintWriter pw = new PrintWriter(fileName);
	    	
	    	pw.println("=============== 키오스크 일별 판매 통계 ===============");
			
			for(Entry<LocalDate, Map<Product, Integer>> entry : transactionMap.entrySet()) {
				LocalDate date = entry.getKey();
				int year = date.getYear();
				int month = date.getMonthValue();
				int day = date.getDayOfMonth();
				Map<Product, Integer> productTotal = entry.getValue();
			
				pw.println("[" + year + "년 " + month + "월 " + day + "일]");
				pw.println("-----------------------------------------------------");
				
				pw.println("[상품명]\t\t[가격]\t\t[수량]\t\t[매출]");
		        pw.println("-----------------------------------------------------");

		        int totalCount = 0; //총 합계
		        int totalSales = 0; //총 판매개수
		        
				for(Map.Entry<Product, Integer> value : productTotal.entrySet()) {
					Product name = value.getKey(); //음식명
		            int count = value.getValue();  //판매개수
		            int price = name.getPrice();   //가격
		            int sales = price * count;     //매출

		            pw.printf("%-10s\t%,2d원\t%,10d개\t%,10d원\n", 
		                      name.getName(), price, count, sales);

		            totalCount += count;
		            totalSales += sales;
				}
				pw.println("-----------------------------------------------------");
				pw.printf("총 판매 개수: %d개\n", totalCount);
				pw.printf("총 매출: %d원\n\n", totalSales);
			}
			
			pw.close();
	        System.out.println("일별 통계 파일이 저장되었습니다: " + fileName);
	    } catch (Exception e) {
	        System.out.println("파일 저장 중 오류 발생: " + e.getMessage());
	    } 
	}
}
