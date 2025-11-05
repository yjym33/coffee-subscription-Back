# Bean Bliss Coffee Subscription Backend

커피 구독 서비스 백엔드 API 서버입니다.

## 기술 스택

- **Java 25 LTS**
- **Spring Boot 3.5.4**
- **Spring Data JPA** + **Querydsl 5.1.0**
- **PostgreSQL 15+**
- **Gradle**
- **Flyway** (데이터베이스 마이그레이션)
- **JUnit 5** + **Testcontainers** (테스트)
- **Micrometer** + **OpenTelemetry Exporter** (모니터링)
- **OpenAPI / Swagger** (API 문서화)
- **JWT** (인증)

## 주요 기능

- ✅ 사용자 회원가입 및 로그인
- ✅ JWT 기반 인증/인가
- ✅ Spring Security 통합
- ✅ OpenAPI/Swagger 문서화
- ✅ Flyway를 통한 데이터베이스 마이그레이션
- ✅ Querydsl을 통한 타입 안전한 쿼리 작성
- ✅ Testcontainers를 통한 통합 테스트
- ✅ Micrometer + OTel을 통한 메트릭 수집

## 실행 방법

### 사전 요구사항

- Java 25 LTS
- PostgreSQL 15+
- Gradle 7.6+

### 데이터베이스 설정

1. PostgreSQL 데이터베이스 생성:
```sql
CREATE DATABASE beanbliss;
CREATE USER beanbliss WITH PASSWORD 'beanbliss';
GRANT ALL PRIVILEGES ON DATABASE beanbliss TO beanbliss;
```

2. `application.properties`에서 데이터베이스 연결 정보 확인

### 애플리케이션 실행

```bash
# Gradle Wrapper 사용
./gradlew bootRun

# 또는 빌드 후 실행
./gradlew build
java -jar build/libs/coffee-subscription-back-0.0.1-SNAPSHOT.jar
```

### 테스트 실행

```bash
# 모든 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests "AuthServiceTest"
```

## API 엔드포인트

### 인증

- `POST /api/auth/signup` - 회원가입
- `POST /api/auth/signin` - 로그인

### API 문서

- Swagger UI: http://localhost:8080/api/docs-ui
- OpenAPI JSON: http://localhost:8080/api/docs

## 프로젝트 구조

```
src/
├── main/
│   ├── java/com/beanbliss/subscription/
│   │   ├── config/          # 설정 클래스
│   │   ├── controller/       # REST 컨트롤러
│   │   ├── dto/              # 데이터 전송 객체
│   │   ├── entity/           # JPA 엔티티
│   │   ├── exception/        # 예외 처리
│   │   ├── repository/       # 데이터 접근 레이어
│   │   ├── security/         # 보안 설정
│   │   └── service/          # 비즈니스 로직
│   └── resources/
│       ├── db/migration/     # Flyway 마이그레이션 파일
│       └── application.properties
└── test/
    └── java/                  # 테스트 코드
```

## 설정

### 환경 변수

주요 설정은 `application.properties`에서 관리됩니다:

- `spring.datasource.*` - 데이터베이스 연결 설정
- `app.jwt.secret` - JWT 시크릿 키
- `app.jwt.expiration-ms` - JWT 만료 시간
- `app.cors.allowed-origins` - CORS 허용 오리진

### 프로파일

- `local` - 로컬 개발 환경 (`application-local.properties`)
- `test` - 테스트 환경 (`application-test.properties`)

## 개발 가이드

### Querydsl 사용

Querydsl을 사용하여 타입 안전한 쿼리를 작성할 수 있습니다:

```java
@Autowired
private JPAQueryFactory queryFactory;

public List<User> findUsersByEmail(String email) {
    QUser user = QUser.user;
    return queryFactory
        .selectFrom(user)
        .where(user.email.eq(email))
        .fetch();
}
```

### Flyway 마이그레이션

새 마이그레이션 파일은 `src/main/resources/db/migration/`에 생성합니다:

- 파일명 형식: `V{version}__{description}.sql`
- 예: `V2__Add_products_table.sql`

## 모니터링

### Actuator 엔드포인트

- Health: http://localhost:8080/api/actuator/health
- Metrics: http://localhost:8080/api/actuator/metrics
- Prometheus: http://localhost:8080/api/actuator/prometheus

### OpenTelemetry

메트릭은 OTLP 프로토콜을 통해 수집됩니다. 기본 설정:

- OTLP Endpoint: `http://localhost:4318/v1/metrics`

## 라이선스

MIT License

# coffee-subscription-Back
