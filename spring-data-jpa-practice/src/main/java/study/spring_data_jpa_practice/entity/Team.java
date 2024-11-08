package study.spring_data_jpa_practice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name"})
public class Team extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String name;

//    FK가 없는 쪽에 mappedBy를 거는 것이 좋음
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }
}
