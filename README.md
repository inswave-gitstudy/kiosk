

# KIOSK
인스웨이브 1차 프로젝트 - 1조

### Git Clone 으로 프로젝트 받아오기

   ```bash
   git clone https://github.com/inswave-gitstudy/kiosk.git
   ```

### Eclipse에서 프로젝트 불러오기

Maven 프로젝트이므로 아래 과정을 따라 진행해주세요.

1. `Eclipse`에서 `File > Import` 클릭
2. `Maven > Existing Maven Projects` 선택 후 `Next`
3. `Browse` 클릭 후, 클론 받은 프로젝트 폴더 선택
4. `pom.xml` 파일이 자동으로 인식됨 → `Finish` 클릭

### 프로젝트 패키지 구조

```
📦 kiosk
┣ 📂 main
┃ ┣ 📂 model      # 데이터 모델 (Cart, Admin, Product 등)
┃ ┣ 📂 manager    # 비즈니스 로직 처리
┃ ┣ 📂 repository # 데이터 저장 및 관리 (파일/DB 처리)
┃ ┗ 📂 controller # 흐름 제어 (사용자 입력 처리)
┣ 📂 util         # 공통 유틸리티 클래스
┗ 📂 config       # 설정 관련 (선택 사항)
┗ 📂 test         # 테스트 관련 (선택 사항)
```

### 각 패키지 역할 설명

| 패키지명   | 역할 및 설명                                 |
|------------|----------------------------------------------|
| `model`    | 데이터 모델 (Cart, Admin, Product 등)            |
| `service`  | 비즈니스 로직 처리      |
| `repository`| 데이터를 저장하고 불러오는 클래스 (파일, DB) |
| `controller`| 전체 흐름을 조정하는 클래스 (입력 → 처리 → 출력)|
| `util`     | 공통적으로 쓰이는 기능 (예: 파일 읽기/쓰기)  |
| `config`   | 설정 관련 클래스 (예: 환경 설정, 상수)       |

## 기타
`.gitignore` : git이 추적하지 않는 파일 목록을 저장한 파일

`.gitkeep` : 패키지 구조를 위해 생성한 임시 파일

프로젝트를 실행하는 방법이나 설정하는 방법을 여기에 추가해 주세요.


