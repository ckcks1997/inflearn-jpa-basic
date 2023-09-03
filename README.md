## 8/18 공부
- JPA 초기세팅<br>
-> resources/META-INF/persistence.xml에 파일 설정
- JPA의 동작방식
  - EntityManagerFactory를 통해 EntityManager를 생성
  - Transaction을 시작(필수)
  - EntityManager를 통해 Entity를 저장하고 조회
  - EntityManagerFactory를 종료

```java
public class JpaMain {
  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    try {
      //hellojpa.Member member = new hellojpa.Member();
      Member member = em.find(Member.class, 1L);
      member.setId(1L);
      member.setName("HelloB"); //따로 persist하지 않아도 update가 된다.
      tx.commit();
    }catch (Exception e){
      tx.rollback();
    }finally {
      em.close();
    }
    emf.close();

  }
}
``` 
  <br>
- 엔티티 매니저 팩토리는 하나만 생성해서 애플리케이션 전체에서 공유
- 엔티티 매니저는 쓰레드간 공유x
- JPA의 모든 데이터 변경은 트랜잭션 안에서만 실행가능

---
## 8/19 공부
#### 영속성 관리 - 내부 동작 원리
- 영속성 컨텍스트에는 1차 캐시가 있는데, 동일한 트랜잭션 안에서는 1차 캐시에 있는 엔티티를 반환하고, 없으면 DB에서 조회해서 1차 캐시에 저장하고 반환한다.
- 1차 캐시에 있는 값을 가져온 객체끼리 ==으로 비교하면 True가 반환된다.
#### 트랜젝션을 지원하는 쓰기 지연
- 트랜잭션을 커밋할 때까지 INSERT SQL을 모아둔다.
- hibenate 설정에서 batch_size를 지정하면, 지정한 수만큼 모아서 SQL을 실행한다.
```xml
<property name="hibernate.jdbc.batch_size" value="10"/>
```
- batch_size를 지정하지 않으면, 모아서 SQL을 실행하지 않고, 1개씩 SQL을 실행한다.

#### 엔티티 수정
- 트랜잭션을 커밋할 때, 영속성 컨텍스트에 있는 엔티티와 스냅샷을 비교해서 변경된 엔티티를 찾는다.
- 변경된 엔티티는 update SQL을 생성해서 쓰기 지연 SQL 저장소에 등록한다.
- 쓰기 지연 SQL 저장소의 SQL을 DB에 전송한다.
- 영속성 컨텍스트를 초기화한다.
- 영속성 컨텍스트를 초기화하면서 1차 캐시를 비우지 않는다.
- 참고로, em.update(member)같은 코드를 사용하지 않고, 변경감지 기능을 이용해 em을 이용하여 가져온 객체를 수정하는 것만으로 update가 된다.


#### 변경 감지
- flush()는 영속성 컨텍스트의 변경내용을 DB에 반영하는 것이다.
- 1차캐시의 값을 버리는게 아니다. (clear()를 사용하면 1차캐시를 비운다.)


TODO: JPQL 공부할 때 준영속 다시 보기

---
## 8/20 공부
#### 엔티티 매핑
- DB 스키마를 자동 생성하는 기능이 있지만, 실무에서는 사용X. 잘못하면 다 날아감..
```xml
<property name="hibernate.hbm2ddl.auto" value="create" /> 

```
##### hbm2ddl.auto 옵션: create, create-drop, update, validate, none
- create: 기존 테이블 삭제 후 다시 생성 (DROP + CREATE)
- create-drop: create와 같으나 종료시점에 테이블 DROP
- update: 변경분만 반영(운영DB에는 사용하면 안됨)
- validate: 엔티티와 테이블이 정상 매핑되었는지만 확인
- none: 사용하지 않음

#### 필드와 컬럼 매핑
```java

@Entity
@Table(name = "Member")
public class Member {
    @Id
    private Long id;
    @Column(name = "name") //db에는 컬럼명이 name이지만, 객체에는 username으로 쓰고 싶을 때
    private String username;

    @Enumerated(EnumType.STRING) //EnumType.ORDINAL은 숫자로 들어감, enum 순서 바뀌면 망해서 쓰면 안됨.
    private RoleType roleType;
    @Temporal(TemporalType.TIMESTAMP)//DATE, TIME, TIMESTAMP. 날짜, 시간, 둘다
    private Date createdDate;
    //@Temporal(TemporalType.TIMESTAMP)
    // LocalDate,LocalDateTime은 @Temporal을 붙이지 않아도 됨
    private LocalDateTime lastModifiedDate;
    @Lob //varchar보다 큰 타입, String이면 CLOB, byte[]이면 BLOB
    private String description; 
    @Transient //db에 저장하지 않음
    private int temp;
  ...
}
```


#### 기본 키 매핑
- Oralce: 시퀀스, MySQL: auto_increment
- 시퀀스 방식은 미리 여러개를 가져와 메모리에 저장해두고 사용할 수 있다.
- auto_increment 방식은 insert할 때마다 DB에 쿼리를 날려서 값을 가져온다.(persist하면 바로 insert 쿼리를 날리고 id를 가져온다.)

---
## 9/3 공부
#### 실전예제
- @Entity 이름으로 Member를 사용할 수 없다.(버전 업데이트 되면서 바뀐듯)
- @Column에는 length같은 속성을 넣을 수 있다.
- @Table에는 uniqueConstraints나 index같은 속성을 넣을 수 있는데 이런 속성들을 명시해주는 것이 추후 유지보수할 때 좋다.
- 예제:
```java
@Entity
@Table(name = "ORDERS", indexes = {@Index(name = "ORDERS_ID_INDEX", columnList = "ORDER_ID")})
public class Order {
  @Id @GeneratedValue
  @Column(name = "ORDER_ID", length = 50)
  private Long id;
    /*@Column(name = "MEMBER_ID")
    private Long memberId; // <- 이 방식은 뭔가 객체지향스럽지 않다. 관계형 DB에 맞춘 설계, 객체그래프 탐색 불가..*/

  private Member member; // <- 이런 방식이 더 객체지향스럽다.
  
  private LocalDateTime orderDate;
  @Enumerated(EnumType.STRING)
  private OrderStatus status;
}
```