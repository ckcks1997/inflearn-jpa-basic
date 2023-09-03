package jpashop.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ORDERS", indexes = {@Index(name = "ORDERS_ID_INDEX", columnList = "ORDER_ID")})
public class Order {
    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;
    /*@Column(name = "MEMBER_ID")
    private Long memberId; // <- 이 방식은 뭔가 객체지향스럽지 않다. 관계형 DB에 맞춘 설계, 객체그래프 탐색 불가..*/

    private Member member; // <- 이 방식이 더 객체지향스럽다.


    private LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
