package study.spring_data_jpa_practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.spring_data_jpa_practice.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsername(String username);
}
