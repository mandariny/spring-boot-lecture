package study.spring_data_jpa_practice.dto;

import lombok.Data;
import study.spring_data_jpa_practice.entity.Member;

// 단순 DTO가 아닌 엔티티에는 @Data 사용하지 않기
@Data
public class MemberDto {
    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    // Entity는 DTO를 가급적 보지 않는 것이 좋음
    // DTO는 Entity를 봐도 상관 없음
    // Entity는 Application 내부에서 공용으로 사용되는 것
    public MemberDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
    }
}
