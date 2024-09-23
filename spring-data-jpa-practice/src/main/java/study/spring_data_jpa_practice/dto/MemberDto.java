package study.spring_data_jpa_practice.dto;

import lombok.Data;

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
}
