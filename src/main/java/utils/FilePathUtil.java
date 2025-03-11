package utils;

import java.io.File;

public class FilePathUtil {

    // 운영체제에 맞는 기본 경로를 반환하는 메서드
    public static String getBaseDirectory() {
        String os = System.getProperty("os.name").toLowerCase();
        String userName;

        // 사용자 이름 추출
        if (os.contains("mac") || os.contains("nix") || os.contains("nux")) {
            // macOS 및 Linux 계열 시스템
            userName = System.getenv("USER"); // macOS 및 Linux에서 사용자 이름 추출
            return "/Users/" + userName + "/kiosk/";
        } else if (os.contains("win")) {
            // Windows 시스템
            userName = System.getenv("USERPROFILE"); // Windows에서 USERPROFILE 경로로부터 사용자 이름 추출
            if (userName == null) {
                userName = System.getenv("HOMEPATH");
            }
            return userName + File.separator + "Desktop" + File.separator + "kiosk" + File.separator;
        } else {
            // 기타 운영체제에 대해 기본 경로 제공
            return "./kiosk/";
        }
    }

    // 지정된 경로에 폴더가 존재하지 않으면 생성하는 메서드
    public static void createDirectoryIfNotExists(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("폴더가 성공적으로 생성되었습니다: " + path);
            } else {
                System.out.println("폴더 생성 실패: " + path);
            }
        }
    }
}
