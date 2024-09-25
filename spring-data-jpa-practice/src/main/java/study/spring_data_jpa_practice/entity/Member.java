package study.spring_data_jpa_practice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
// 실무에서는 Entity에 되도록 Setter를 사용하지 않음
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// team은 출력하면 안됨. 연관관계를 타고 출력하게 돼 무한루프에 빠질 수 있음
@ToString(of = {"id", "username", "age"})
//@NamedQuery(
//        name="Member.findByUsername",
//        query="select m from Member m where m.username = :username"
//)
// JPA 표준 스펙
// 자주 사용하진 않음
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

//    Member와 Team은 N:1 관계. N 쪽에서 FK를 가지고 있어야 함
//    JPA의 모든 연관관계는 지연로딩으로 세팅해야 성능 최적화가 쉬움
    // Member를 조회하는 시점에 Team은 가짜 객체로 만들어놓고, 실제로 Team을 조회할 일이 생길 경우 추가로 조회
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

//    Entity는 기본적으로 default 생성자가 있어야 함. private는 안됨 최소 protected 사용 -> JPA의 프록시 기술을 사용할 떄 필요
//    protected Member() {
//    }

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

//    setter보다는 필요한 경우 명확한 메소드 이름으로 기능을 구현하는 것이 좋음
//    public void changeUsername(String username) {
//        this.username = username;
//    }

//    연관관계 세팅을 위한 메서드
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
