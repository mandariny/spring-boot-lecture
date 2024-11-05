package study.spring_data_jpa_practice.repository;

import ch.qos.logback.core.util.StringUtil;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import study.spring_data_jpa_practice.entity.Member;
import study.spring_data_jpa_practice.entity.Team;

/*
 * Specification은 실무에서 거의 사용하지 않음
 * 동적 쿼리 문제를 해결하기 위한 방안 -> 실무에선 QueryDSL을 주로 사용
 */
public class MemberSpec {
    public static Specification<Member> teamName(final String teamName) {
        return (root, query, builder) -> {
            if (StringUtils.isEmpty(teamName)) {
                return null;
            }

            Join<Member, Team> t = root.join("team", JoinType.INNER);// 회원과 조인
            return builder.equal(t.get("name"), teamName);
        };
    }

    public static Specification<Member> username(final String username) {
        return (root, query, builder) -> builder.equal(root.get("username"), username);
    }
}
