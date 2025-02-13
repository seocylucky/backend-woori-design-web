# 🏗️ Woori Design Web - Backend 

<br />

## 📝 소개
WOORI DESIGN 시스템의 웹 사이트를 위한 백엔드 repository입니다.

<br />

### 📂 프로젝트 구조

```markdown
backend-woori-design-web/
│── src/
│   ├── main/
│   │   ├── java/woori_design_web/backend_woori_design_web/
│   │   │   ├── api/            # REST API 컨트롤러
│   │   │   ├── dto/            # DTO (Data Transfer Object)
│   │   │   ├── entity/         # JPA 엔티티 클래스
│   │   │   ├── enums/          # ENUM
│   │   │   ├── repository/     # JPA Repository 인터페이스
│   │   │   ├── security/       # JWT 인증 및 보안 관련 클래스
│   │   │   ├── service/        # 비즈니스 로직을 처리하는 서비스 클래스 (interface, impl)
│   ├── test/                   # 테스트 코드
│── build.gradle                # Gradle 빌드 파일
│── application.yml             # Spring Boot 설정 파일
```

<br />

## ✨ 주요 기능  

### 🔑 **인증 & 보안**  
- **JWT 기반 로그인/로그아웃**  
- **Access Token & Refresh Token 관리**  

### 📝 **사용자 관리**  
- 회원 가입  
- 회원 정보 조회 및 수정  

### 📄 **댓글 기능**  
- 댓글 추가 및 삭제

### ♥️ **좋아요 기능**  
- 좋아요 추가 및 삭제  

<br />

## 🛠️ 사용 기술  

| 기술 스택  | 설명 |
|------------|----------------------------------------|
| **Java 17** | 최신 Java 버전 사용 |
| **Spring Boot 3** | 백엔드 프레임워크 |
| **Spring Data JPA (Hibernate)** | 데이터베이스 ORM |
| **MySQL** | 개발 환경에서 MySQL 사용 |
| **Spring Boot Validation** | 데이터 유효성 검증 |
| **JWT (jjwt)** | 사용자 인증 및 토큰 관리 |
| **BCrypt (org.mindrot.jbcrypt)** | 비밀번호 암호화 |
| **Spring Data Redis** | 캐싱 및 세션 관리 |
| **Docker & Docker Compose** | 컨테이너 기반 배포 환경 |
| **Lombok** | 코드 간소화를 위한 라이브러리 |
| **Gradle** | 프로젝트 빌드 및 의존성 관리 |
| **JUnit 5** | 테스트 프레임워크 |


