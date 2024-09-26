package study.spring_data_jpa_practice.repository;

import study.spring_data_jpa_practice.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
