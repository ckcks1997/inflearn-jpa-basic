package hellojpa;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name") //db에는 컬럼명이 name이지만, 객체에는 username으로 쓰고 싶을 때
    private String username;

   @Enumerated(EnumType.STRING) //EnumType.ORDINAL은 숫자로 들어감
    private RoleType roleType;
    @Temporal(TemporalType.TIMESTAMP)//DATE, TIME, TIMESTAMP. 날짜, 시간, 둘다
    private Date createdDate;
    //@Temporal(TemporalType.TIMESTAMP)//LocalDate,LocalDateTime은 @Temporal을 붙이지 않아도 됨
    private LocalDateTime lastModifiedDate;
    @Lob //varchar보다 큰 타입, String이면 CLOB, byte[]이면 BLOB
    private String description;
    @Transient //db에 저장하지 않음
    private int temp;

    public Member() {
    }

    public Member(Long id, String username) {
        this.id = id;
        this.username = username;
    }
//Getter, Setter …


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }
}