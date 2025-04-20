# 🧊 냉장고 정리 도우미 – Fridge Helper(SERVER)

> 1인 가구를 위한 스마트 냉장고 정리 도우미  
> 유통기한 관리, 가족 단위 공유, 알림 기능까지 지원하는 냉장고 관리 서비스입니다.

---

## 🔗 관련 링크

- 📱 프론트엔드 Flutter 앱 GitHub: [https://github.com/EstelLa-83/FridgeHelper](https://github.com/EstelLa-83/FridgeHelper)

---
## 📱 주요 기능

### 사용자
- 이메일 기반 회원가입 및 로그인 (JWT 인증)
- 사용자 정보 조회, 이름 수정, 비밀번호 변경

### 가족 그룹
- 가족 생성 및 구성원 목록 조회
- 초대/수락/거절을 통한 가족 이동
- 구성원이 0명이면 그룹 자동 삭제

### 냉장고 & 음식 관리
- 냉장고 생성/수정/삭제
- 냉장고별 음식 등록/조회/수정/삭제
- 가족 그룹 전체 냉장고의 음식 통합 조회
- 유통기한 기준 정렬 기능 포함

### 초대 시스템
- 이메일 기반 가족 초대
- 자기 자신/동일 가족 초대 방지
- 초대 중복 방지 및 상태 관리 (PENDING, ACCEPTED, DECLINED)

---

## ⚙️ 기술 스택

| 항목 | 내용 |
|------|------|
| Language | Kotlin 1.9.25 |
| Java 버전 | Java 21 |
| Framework | Spring Boot 3.4.1 |
| Build Tool | Gradle (Kotlin DSL) |
| ORM | Spring Data JPA |
| 문서화 | Springdoc OpenAPI 2.7.0 (Swagger UI) |
| DB | MySQL 8.0.33 / H2 (file 모드 개발용) |
| 인증 | JWT (jjwt 0.11.5) |
| 테스트 | JUnit5 (kotlin-test-junit5) |

---

## 🧪 API 문서

- Swagger UI: http://localhost:8080/swagger-ui.html  
- 테스트 시 Bearer 토큰을 입력하여 인증 기반 API 호출 가능

---

## 🗃️ 프로젝트 구조 (백엔드)

📦fridge_server  
 ┣ 📂domain  
 ┃ ┣ 📂auth  
 ┃ ┣ 📂user  
 ┃ ┣ 📂family  
 ┃ ┣ 📂fridge  
 ┃ ┣ 📂food  
 ┃ ┗ 📂familyinvite  
 ┣ 📂config  
 ┣ 📂common  
 ┗ 📂endpoint  

---

## 🚀 실행 방법

1. application.yml 파일 환경 설정  
   프로젝트에는 H2와 MySQL 모두 사용 가능하도록 구성되어 있으며,  
   사용하려는 데이터베이스에 따라 아래와 같이 application.yml을 수정해야 합니다.

📌 H2 데이터베이스 (개발/테스트용)
```
spring:
  datasource:
    url: jdbc:h2:file:./data/fridge-db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password:
```

📌 MySQL 데이터베이스 (운영용)
```
spring:
  datasource:
    url: jdbc:mysql://[자신의 DB 주소]:3306/fridge_db
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: [자신의 사용자명]
    password: [자신의 비밀번호]
```

⚠️ 각자의 환경(DB 주소, 포트, 계정 정보)에 맞게 반드시 수정해야 정상 실행됩니다.

2. Gradle 빌드
```
./gradlew build
```

3. 서버 실행
```
./gradlew bootRun
```

## 📌 기타 참고 사항

- 개발 중 H2 콘솔 접속 및 Swagger 테스트를 위해 Spring Security에서  
  `/h2-console/**`, `/swagger-ui/**` 경로에 대한 예외 설정을 구성하였습니다.  
- 예외 응답 구조는 Swagger와의 호환성 문제로 문서화가 제한되어 있어,  
  별도의 에러 코드 문서를 팀원과 공유하였습니다.
