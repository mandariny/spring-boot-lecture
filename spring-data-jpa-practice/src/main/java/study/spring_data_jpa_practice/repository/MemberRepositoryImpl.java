package study.spring_data_jpa_practice.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import study.spring_data_jpa_practice.entity.Member;

import java.util.List;

@RequiredArgsConstructor
// MemberRepository Interface와 이름을 맞춘 후 Impl을 붙여야 함 (규칙 & 관례) -> SpringData Framework가 구현체를 찾아서 call해주기 때문
/*
*
* 실무에서 인터페이스만으로 해결되지 않는 경우 구현 사용
* QueryDSL 등을 구현하거나 Spring JDBC Template 등을 함께 사용할 때 활용할 수 있음
*
*/
public class MemberRepositoryImpl implements MemberRepositoryCustom{
    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
