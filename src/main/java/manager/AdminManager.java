package manager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

import model.Admin;
import utils.FilePathUtil;

public class AdminManager {
	private Scanner sc;
	private Admin admin; 
	private String salt; //로그인시에 사용할 salt
	
	//생성자
	public AdminManager() {
		this.admin = new Admin();
		this.sc = new Scanner(System.in);
		this.salt = saltLoad();
		
		if(admin.getPassword() == null) {
			initializePassword("admin74");
		}
	}
	
	//로그인 기능
    public boolean checkLoginCredentials(String inputPassword) {	
        String hashPassword = encryptPassword(inputPassword.trim(), this.salt);
  
        if(hashPassword.equals(admin.getPassword()))
        	return true;
        else
        	return false;
    }

    //비밀번호 수정하는 기능
    public void modifyPassword() {
    	System.out.print("현재 비밀번호를 입력하세요(취소: n)>> ");
    	String password = sc.nextLine().trim();
    	
    	if(password.equals("n") || password.equals("N")) {
    		return;
    	}
    	else if(checkLoginCredentials(password)) {
    		printModifyPassword(); //비밀번호 조건메뉴 표시
    		
    		while(true) {
    			System.out.print("새로운 비밀번호를 입력하세요(취소: n)>> ");
            	password = sc.nextLine().trim();
            	
            	if(password.equals("n") || password.equals("N")) {
            		break;
            	}
            	else if(checkPassword(password)) {
    				String salt = generateSalt();
    				String newHashPassword = encryptPassword(password, salt);
    	        	savePasswordFile(newHashPassword, salt); //비밀번호를 암호화후에 파일에 저장
    	        	admin.setPassword(newHashPassword);//Admin 클래스에 있는 비밀번호를 새로운 비밀번호로 수정
    	        	
    	        	System.out.println("비밀번호 수정되었습니다.");
    	        	break;
    			}
    		}
    	}else {
    		System.out.println("잘못된 비밀번호입니다.");
    	}
    }
    
    //비밀번호 변경 세부조건 표시 기능
    private void printModifyPassword() {
    	System.out.println("\n****************비밀번호 변경 조건****************");
		System.out.println("비밀번호는 공백은 없어야 하며, 7자리 ~ 12자리 사이여야 합니다");
		System.out.println("또한 영문자, 숫자, 특수문자를 조합하여 작성해야 합니다\n");
    }

    //비밀번호 수정시에 정규식으로 조건 체크하는 기능
    private boolean checkPassword(String str) {
		String checkPassword = str.trim().replaceAll(" ", "");
		
		if(!str.equals(checkPassword)) {//비밀번호에 공백이 있을 때
			System.out.println("비밀번호에 공백이 있으면 안됩니다");
			return false;
		}
		else if(str.length() < 7) {//비밀번호가 7자 미만, 12자 초과일 때
			System.out.println("비밀번호는 7자리 이상이어야 합니다");
			return false;
		}
		else if(str.length() > 12) {//비밀번호가 7자 미만, 12자 초과일 때
			System.out.println("비밀번호는 12자리 이하여야 합니다");
			return false;
		}
		else if(!str.matches("^(?=.*[a-zA-Z])(?=.*[~!@#$%^&*+=()_-])(?=.*[0-9]).+$")) {//특수문자 사용
			System.out.println("비밀번호는 영문자, 숫자, 특수문자를 조합하여 작성해야 합니다");
			return false;
		}
		else if(str.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
			System.out.println("비밀번호는 한글을 사용하면 안됩니다");
			return false;
		}
		else if(!checkRepetition(str)) {//동일한 문자를 연속적으로 사용하는 것 ex) 111, aaa
			System.out.println("동일한 문자를 연속적으로 3번이상 사용할 수 없습니다");
			return false;
		}
		else if(!checkConsecutive(str)) {//연속적인 값을 사용하는 경우 ex) 123, abc
			System.out.println("연속적인 값을 사용할 수 없습니다 ex) 123, abc");
			return false;
		}
		return true;
	}
	
	//동일한 값이 있는지 확인 ex) aaadkjf -> 이런식으로 같은 값을 3번 이상 쓸 수 없음
	private boolean checkRepetition(String passwrod) {
		//연속적인 값이 3개 있다면 false를 리턴함
		for(int i=0; i < passwrod.length()-3; i++) {
			if((passwrod.charAt(i) == passwrod.charAt(i+1)) && (passwrod.charAt(i+1) == passwrod.charAt(i+2))){
				return false;
			}
		}
		return true;
	}
	
	//123 또는 abc이렇게 연속적으로 사용할 수 없음
	private boolean checkConsecutive(String passwrod) {
		//연속적인 값이 3개 있다면 false를 리턴함
		for(int i=0; i < passwrod.length()-3; i++) {
			if((passwrod.charAt(i)+1 == passwrod.charAt(i+1)) && (passwrod.charAt(i+1)+1 == passwrod.charAt(i+2))){
				return false;
			}
		}
		return true;
	}
    
    //초기에 비밀번호 초기화 하는 기능
    private void initializePassword(String password) {
    	String salt = generateSalt();
    	String pin = encryptPassword(password, salt);
    	savePasswordFile(pin, salt);
    	admin.setPassword(pin);
    }
    
    // 비밀번호를 SHA-256으로 암호화하는 메서드, 단방향 암호화 방법이라 복호화가 불가능
    private String encryptPassword(String password, String salt)  { 
        MessageDigest digest = null;
        
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.out.println("암호화 과정중에 문제가 발생했습니다" + e.getMessage());
		}
		
        byte[] hashBytes = digest.digest((password+salt).getBytes());
        
        // Base64로 인코딩하여 문자열로 반환
        return Base64.getEncoder().encodeToString(hashBytes);
    }
    
    // 암호화된 비밀번호를 파일에 저장
    private void savePasswordFile(String password, String salt) {
    	String FileName = FilePathUtil.getBaseDirectory() + "AdminPassword.txt";
    	FileWriter fw = null;
    	BufferedWriter bw = null;
    	
    	String FileName2 = FilePathUtil.getBaseDirectory() + "Salt.txt";
    	FileWriter fw2 = null;
    	BufferedWriter bw2 = null;
    	
        try {
        	fw = new FileWriter(FileName);
        	bw = new BufferedWriter(fw);
            bw.write(password);
    
            fw2 = new FileWriter(FileName2);
        	bw2 = new BufferedWriter(fw2);
            bw2.write(salt);
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류가 발생했습니다.");
            e.printStackTrace();
        }finally {
        	try {
        		bw2.close();
        		fw2.close();
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
    
    //Salt 생성
    private String generateSalt() {
		//랜덤 salt 생성
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[20];
		
		//난수를 생성
		random.nextBytes(salt);
		
		//문자열로 변환
		StringBuilder sb = new StringBuilder();
		for(byte b : salt) {
			sb.append(String.format("%02x", b));
		}
		
		return sb.toString();
	}

    //salt를 로드해서 저장하는 기능
    private String saltLoad() {
    	File file = new File(FilePathUtil.getBaseDirectory()+"Salt.txt");
    	FileReader fr = null;
		BufferedReader br = null;
		String salt = "";
		
		if(!file.exists()) {
			return null;
		}
		
		try {
			fr = new FileReader(file);//비밀번호 저장한 파일
			br = new BufferedReader(fr);
			
			salt = br.readLine();
		} catch (Exception e) {
			System.out.println("비밀번호 기능에 문제가 발생했습니다." + e.getMessage());
		}finally {
			try {
				br.close();
				fr.close();
			} catch (Exception e2) {
				
			}
		}
    	return salt; //파일에 저장되어있는 암호화된 Salt를 리턴
    }

}
