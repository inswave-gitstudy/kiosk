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
import utils.FilePathUtil;

public class SalesDataManager {
	private SalesData salesData;
	private FilePathUtil filePathUtil;

	//생성자
	public SalesDataManager(SalesData salesData) {
		this.salesData = salesData;
		this.filePathUtil = new FilePathUtil();
	}

	// 파일 이름 리턴하는 기능
	private String fileTitleFormat() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		return filePathUtil.getBaseDirectory() + "키오스크_통계파일_" + formatter.format(now) + ".csv";
	}

	//CSV파일로 저장함
	public void saveFileToCSV(TreeMap<String, Map<Integer, Integer>> salesStats, String type) {
		String fileName = fileTitleFormat(); //저장할 파일명
		File file = new File(fileName); //파일
		BufferedWriter bufferedWriter = null; //버퍼
		OutputStreamWriter outputStreamWriter = null;
		FileOutputStream fileOutputStream = null;

		try {
			fileOutputStream = new FileOutputStream(file);
			outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8); //한글깨짐 방지를 위해서 추가
			bufferedWriter = new BufferedWriter(outputStreamWriter);
			
			// BOM 추가 (UTF-8로 저장할 때 한글 깨짐을 방지)
			bufferedWriter.write("\uFEFF");

			// CSV 파일 헤더
			bufferedWriter.write("날짜,상품 ID,상품명,판매 개수,단가,판매 금액\n");

			// salesStats 데이터를 순회하며 CSV로 작성
			for (Map.Entry<String, Map<Integer, Integer>> entry : salesStats.entrySet()) {
				String date = entry.getKey(); // 날짜
				Map<Integer, Integer> productSales = entry.getValue(); //id값 , 판매개수

				for (Map.Entry<Integer, Integer> productEntry : productSales.entrySet()) {
					int productId = productEntry.getKey(); //상품ID
					int quantity = productEntry.getValue(); //판매개수

					// Product 객체 가져와서 가격 및 이름 조회
					Product product = salesData.getProductById(productId);
					int price = (product != null) ? product.getPrice() : 0;
					String productName = (product != null) ? product.getName() : "알수없음";
					int salesAmount = price * quantity; //총 판매금액

					// CSV 파일에 데이터 기록
					bufferedWriter.write(String.format("%s,%d,%s,%d,%d,%d\n", date, productId, productName, quantity, price,
							salesAmount));
				}
			}

			System.out.println("CSV 파일로 저장되었습니다: " + fileName);

		} catch (IOException e) {
			System.out.println("파일 저장 중 오류가 발생했습니다: " + e.getMessage());
		} finally {
			//버퍼닫기
			try {
				bufferedWriter.close();
				outputStreamWriter.close();
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
