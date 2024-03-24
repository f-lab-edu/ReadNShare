# ReadNShare
독서 기록 및 공유 서비스

# **요약 (Summary)**

이 프로젝트는 독서 기록 및 공유 서비스를 만드는 것으로, 사용자는 회원 가입 후 독서 기록을 작성하고 다른 사용자들의 기록을 탐색할 수 있습니다.

팔로잉 피드를 통하여 다양한 독서 기록을 조회할 수 있으며, 다른 사용자가 팔로잉 하는 경우 이에 대한 알림을 받을 수 있습니다.

# **배경 (Background)**

이 프로젝트는 독서를 즐기며 이를 기록하고 공유하고자 하는 사용자들을 위하여 기획 되었습니다.

많은 독서 애호가들이 독서 경험을 공유하고 다른 사람들의 추천을 받고 싶어하는데, 이를 위한 플랫폼이 부족하다고 생각했습니다.

따라서 독서 커뮤니티를 구축하여 사용자들이 서로의 독서 경험을 나누고 소통할 수 있는 공간을 제공하고자 합니다.

# **목표 (Goals)**

1. 안정적이고 확장 가능한 독서 기록 및 공유 서비스 구현
    1. 유지보수와 확장이 용이한 코드를 작성, 객체지향 프로그래밍 원리에 대한 학습
    2. 단위 테스트를 통해 코드의 안정성을 확보, TDD에 대한 학습
2. CI/CD를 통한 자동화된 빌드 및 배포 과정을 구축하여 원활한 협업 배경에 대한 이해

# **목표가 아닌 것 (Non-goals)**

1. 책 검색 기능 정렬 및 필터링 구현

# **계획 (Plan)**

### 시스템 아키텍처
![아키텍처2 drawio](https://github.com/f-lab-edu/ReadNShare/assets/114924775/5ab1d25c-66bb-43c6-97f9-aa2676c4f56a)

### 사용할 기술

- Java 17
- Spring Boot 3.2.2
- Spring Data JPA
- Firebase Admin SDK
- MySQL
- Redis
- Docker
- Naver Cloud Platform

### 기술 상세

- 로그인: JWT, Redis 이용하여 구현
    
1. 로그인 성공 후 서버에서 Access Token, Refresh Token 생성, Refresh Token은 Redis에 저장한다.
    1. 생성된 Access Token은 Header로 응답 받음
    2. 생성된 Refresh Token은 HttpOnly 옵션 쿠키로 응답 받음
    3. 새로고침 하는 경우 Refresh Token을 통해 Access Token 발급 요청
        

2. 이후 클라이언트에서 API 요청 시 서버에서 Access Token을 검증한다.
   1. Access Token 만료 시 클라이언트에 만료 응답 보냄 (Refresh Token 보낼 것을 요청)
   2. 클라이언트에서 만료 응답 받은 후 Refresh Token으로 서버에 재발급 요청
   3. 서버에서 Refresh Token 받은 후 확인하여 검증
   4. Refresh Token 일치하면 Access Token 재발급, 일치하지 않는다면 에러 발생

```mermaid
sequenceDiagram
    participant A as Client
    participant B as Server
    participant C as DB
    
    A->>B: 로그인
    B->>B: Access/Refresh 토큰 생성
    B->>C: Refresh 토큰 저장
    B->>A: Access/Refresh 토큰 전달(Http Header, Cookie)
    A->>B: Access 토큰 만료 시 재발급 요청(+Refresh Token)
    B->>C: Refresh 토큰 검증
    B->>A: Refresh 토큰 일치 시 Access 토큰 재발급하여 응답
```	

- 도서 검색 (목록): 네이버의 도서 검색 오픈API 이용하여 구현
  - 인증정보 발급 받아 API 호출 시 HTTP 헤더에 포함 후 전송해야 한다.
1. 클라이언트에서 검색 키워드로 도서를 검색한다.
2. 서버에서 API 호출 후 검색된 도서 리스트를 응답한다.

# **이외 고려사항들 (Other Considerations)**
- 내 서재(마이 페이지)
    - 팔로우 목록
        - 팔로잉 / 팔로워 목록 조회가 가능하다.
    - 도서 목록
        - 책 검색 후 내 서재에 담은 책들의 전체 목록 조회가 가능하다.
    - 독서 기록 목록
        - 내가 작성한 독서 기록 목록 조회가 가능하다.

- 댓글 기능
