package manager;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

import model.Admin;

public class AdminManager {
	Scanner sc;
	private Admin admin;
	
	public AdminManager() {
		this.admin = new Admin();
		this.sc = new Scanner(System.in);
		
		if(admin.getPassword() == null) {
			initializePassword("admin74");
		}
	}
	
	//로그인 기능
    public boolean checkLoginCredentials(String inputPassword) {
        String hashPassword = encryptPassword(inputPassword);

        if(hashPassword.equals(admin.getPassword()))
        	return true;
        else
        	return false;
    }

    //비밀번호 수정하는 기능
    public void modifyPassword() {
    	System.out.print("현재 비밀번호를 입력하세요>> ");
    	String password = sc.nextLine();
    	
    	if(checkLoginCredentials(password)) {
    		while(true) {
    			System.out.print("새로운 비밀번호를 입력하세요>> ");
            	password = sc.nextLine();
            	
    			if(checkPassword(password.trim())) {
    				String newHashPassword = encryptPassword(password.trim());
    	        	savePasswordFile(newHashPassword); //비밀번호를 암호화후에 파일에 저장
    	        	admin.setPassword(newHashPassword);//Admin 클래스에 있는 비밀번호를 새로운 비밀번호로 수정
    	        	
    	        	System.out.println("비밀번호 수정되었습니다.");
    	        	break;
    			}
    		}
    	}else {
    		System.out.println("잘못된 비밀번호입니다.");
    	}
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
    public void initializePassword(String password) {
    	String pin = encryptPassword(password);
    	savePasswordFile(pin);
    	admin.setPassword(pin);
    }
    
    // 비밀번호를 SHA-256으로 암호화하는 메서드
    private String encryptPassword(String password)  { 
        MessageDigest digest = null;
        
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.out.println("암호화 과정중에 문제가 발생했습니다" + e.getMessage());
		}
		
        byte[] hashBytes = digest.digest(password.getBytes());
        
        // Base64로 인코딩하여 문자열로 반환
        return Base64.getEncoder().encodeToString(hashBytes);
    }
    
    // 암호화된 비밀번호를 파일에 저장
    private void savePasswordFile(String password) {
    	String FileName = "AdminPassword.txt";
    	FileWriter fw = null;
    	BufferedWriter bw = null;
    	
        try {
        	fw = new FileWriter(FileName);
        	bw = new BufferedWriter(fw);
            bw.write(password);
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류가 발생했습니다.");
            e.printStackTrace();
        }finally {
        	try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
}
