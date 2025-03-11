package manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

import model.Product;
import model.SalesData;

public class SalesDataManager {
	private SalesData salesData;

	public SalesDataManager(SalesData salesData) {
		this.salesData = salesData;
	}

	// 파일 이름 리턴하는 기능
	private String fileTitleFormat() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		return "C:\\Temp\\키오스크_통계파일_" + formatter.format(now) + ".csv";
	}

	public void saveFileToCSV(TreeMap<String, Map<Integer, Integer>> salesStats, String type) {
		// 저장할 파일명
		String fileName = fileTitleFormat();
		File file = new File(fileName);

		try (BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

			// BOM 추가 (UTF-8로 저장할 때 한글 깨짐을 방지)
			writer.write("\uFEFF");

			// CSV 파일 헤더
			writer.write("날짜,상품 ID,상품명,판매 개수,단가,판매 금액\n");

			// salesStats 데이터를 순회하며 CSV로 작성
			for (Map.Entry<String, Map<Integer, Integer>> entry : salesStats.entrySet()) {
				String date = entry.getKey(); // 날짜
				Map<Integer, Integer> productSales = entry.getValue();

				for (Map.Entry<Integer, Integer> productEntry : productSales.entrySet()) {
					int productId = productEntry.getKey();
					int quantity = productEntry.getValue();

					// Product 객체 가져와서 가격 및 이름 조회
					Product product = salesData.getProductById(productId);
					double price = (product != null) ? product.getPrice() : 0.0;
					String productName = (product != null) ? product.getName() : "Unknown Product";
					double salesAmount = price * quantity;

					// CSV 파일에 데이터 기록
					writer.write(String.format("%s,%d,%s,%d,%.2f,%.2f\n", date, productId, productName, quantity, price,
							salesAmount));
				}
			}

			System.out.println("CSV 파일로 저장되었습니다: " + fileName);

		} catch (IOException e) {
			System.out.println("파일 저장 중 오류가 발생했습니다: " + e.getMessage());
		}
	}

}
