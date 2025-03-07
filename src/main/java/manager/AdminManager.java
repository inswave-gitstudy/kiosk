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
    		System.out.print("새로운 비밀번호를 입력하세요>> ");
        	password = sc.nextLine();
        	
        	String newHashPassword = encryptPassword(password);
        	savePasswordFile(newHashPassword); //비밀번호를 암호화후에 파일에 저장
        	admin.setPassword(newHashPassword);//Admin 클래스에 있는 비밀번호를 새로운 비밀번호로 수정
        	
        	System.out.println("비밀번호 수정되었습니다.");
    	}else {
    		System.out.println("잘못된 비밀번호입니다.");
    	}
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
//            System.out.println("비밀번호가 파일에 저장되었습니다.");
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
