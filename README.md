# 8/18 공부
- JPA 초기세팅<br>
-> resources/META-INF/persistence.xml에 파일 설정
- JPA의 동작방식
  - EntityManagerFactory를 통해 EntityManager를 생성
  - Transaction을 시작(필수)
  - EntityManager를 통해 Entity를 저장하고 조회
  - EntityManagerFactory를 종료
<br>
- 엔티티 매니저 팩토리는 하나만 생성해서 애플리케이션 전체에서 공유
- 엔티티 매니저는 쓰레드간 공유x
- JPA의 모든 데이터 변경은 트랜잭션 안에서만 실행가능

