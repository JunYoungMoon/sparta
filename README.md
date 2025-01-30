## spring 기본기 레포지토리입니다.

1. spring-prepare : 메모장 CRUD **[입문 1주차]**
2. spring-auth : 스프링 시큐리티를 활용한 JWT 인증/인가 처리 **[숙련 1주차]**
3. spring-resttemplate : RestTemplate을 활용한 서버간에 데이터 통신 **[숙련 2주차]**
4. jpa-advance : JPA 연관관계 설정방법 **[숙련 2주차]**
5. myselectshop : 3,4를 통합하여 My Select Shop 적용 **[숙련 2주차]**, **[심화 1주차]**
   - naver develop client id,pw 필요 (경로 : /myselectshop/naver/service/NaverApiService.java)
   - 더미 데이터 최초 실행시 한번만 동작 하도록 주석 해제 (경로 : /myselectshop/util/TestDataRunner.java)
   - kakao develop REST API 키 필요 (경로 : /resources/templates/login.html, /myselectshop/service/KakaoService.java) 
   - 심화주차 내용 추가 : 소셜 로그인, 테스트, AOP, Global Exception, Error 메시지 관리
6. junit5-practice :간단한 테스트 방법 자세한건 5번에서 진행 **[심화 1주차]**