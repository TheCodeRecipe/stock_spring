Stock Signal 프로젝트의 메인 백엔드 코드입니다. 
사용자 인증, 데이터 관리, Flask 서버와의 통신을 통해 프로젝트의 중심 역할을 수행합니다.


## **기능**

1. **JWT 인증**
   - 사용자 및 관리자 구분.

2. **Flask 서버와 통신**
   - 관리자가 요청 시 Python(Flask) 서버와 통신하여 데이터 갱신 요청 수행.
   - 갱신된 데이터를 받아 데이터베이스에 저장.

3. **데이터 관리 및 제공**
   - 분석 결과를 데이터베이스에 저장하고 사용자 요청 시 반환.



## **사용 기술**
- **프레임워크**: Spring Boot
- **데이터베이스**: PostgreSQL
- **라이브러리**: Spring Security, Spring Data JPA
- **빌드 도구**: Gradle
- **통신**: REST API

🔗 **[Stock Signal 프론트엔드 리포지토리](https://github.com/TheCodeRecipe/stock-signal)**

🔗 **[Stock Signal 파이썬 리포지토리](https://github.com/TheCodeRecipe/stock-api)**
