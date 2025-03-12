package model;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import utils.FilePathUtil;

public class Admin {
    private String password;//관리자 비밀번호

    //생성자
    public Admin() {
        this.password = passwordLoad();
    } 
    
    //비밀번호 로드하는 기능 //초기 비번 admin74
    private String passwordLoad() {
    	File file = new File(FilePathUtil.getBaseDirectory()+"AdminPassword.txt");
    	FileReader fr = null;
		BufferedReader br = null;
		String password = "";
		
		if(!file.exists()) {
			return null;
		}
		
		try {
			fr = new FileReader(file);//비밀번호 저장한 파일
			br = new BufferedReader(fr);
			
			password = br.readLine();
		} catch (Exception e) {
			System.out.println("비밀번호 기능에 문제가 발생했습니다." + e.getMessage());
		}finally {
			try {
				br.close();
				fr.close();
			} catch (Exception e2) {
				
			}
		}
    	return password; //파일에 저장되어있는 암호화된 비밀번호를 리턴
    }

    //비밀번호 getter
	public String getPassword() {
		return password;
	}
	
	//비밀번호 setter
	public void setPassword(String password) {
		this.password = password;
	}
}
