package study.spring_data_jpa_practice.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.spring_data_jpa_practice.dto.MemberDto;
import study.spring_data_jpa_practice.entity.Member;
import study.spring_data_jpa_practice.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        // 실무에선 값이 없는 경우에 대한 예외처리를 하는 것이 좋음
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        // 카운트 검증
        count = memberRepository.count();
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

//    @Test
//    public void testNamedQuery() {
//        Member m1 = new Member("AAA", 10);
//        Member m2 = new Member("BBB", 20);
//        memberRepository.save(m1);
//        memberRepository.save(m2);
//
//        List<Member> result = memberRepository.findByUsername("AAA");
//        Member findMember = result.get(0);
//
//        assertThat(findMember).isEqualTo(m1);
//    }

    @Test
    public void testQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        Member findMember = result.get(0);

        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }

    }

    @Test
    public void findByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }
    @Test
    public void testReturnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa1 = memberRepository.findListByUsername("AAA");
        Member aaa2 = memberRepository.findMemberByUsername("AAA");
        Optional<Member> aaa3 = memberRepository.findOptionalByUsername("AAA");

        System.out.println("findMember = " + aaa1);
        System.out.println("findMember = " + aaa2);
        System.out.println("findMember = " + aaa3.get());
    }

    @Test
    public void paging() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;

        // 0번 페이지부터 카운드
        // 0페이지에서 3개 가져오기
        // Sorting 조건은 옵션
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
//        Slice<Member> page = memberRepository.findByAge(age, pageRequest);
//        List<Member> page = memberRepository.findByAge(age, pageRequest);

        // Entity는 절~~~대 API로 넘기면 안 됨! DTO로 변환한 후 넘기기
        // Entity에서 쉽게 DTO로 바꾸는 법
        // Page는 유지하면서 DTO로 반환해도 괜찮음 -> 내부 컨텐츠 & totalPage, totalElement 등도 json으로 변환돼서 나감
        Page<MemberDto> toMap = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        // then
//        List<Member> content = page.getContent();
        // 반환 타입이 Page라 totalCount를 세는 쿼리를 자동적으로 날림
//        long totalElements = page.getTotalElements();
//        for (Member member : content) {
//            System.out.println("member = " + member);
//        }
//        System.out.println("totalElements = " + totalElements);
//        assertThat(content.size()).isEqualTo(3);
//        assertThat(page.getTotalElements()).isEqualTo(5);
        // 페이지 번호
//        assertThat(page.getNumber()).isEqualTo(0);
        // 전체 페이지 수
//        assertThat(page.getTotalPages()).isEqualTo(2);
        // 첫 번쨰 페이지인지
//        assertThat(page.isFirst()).isTrue();
        // 다음 페이지가 있는지
//        assertThat(page.hasNext()).isTrue();
        for (Member member : page) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void bulkUpdate() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // when
        int resultCount = memberRepository.bulkAgePlus(20);

        // bulk update 전에 영속성 컨텍스트 내의 데이터는 flush로 먼저 반영함
        // 데이터가 아직 영속성 컨텍스트에 남아있어 bulk update가 반영된 DB와 다른 40살이 뜨게 됨!
        // JPA와 MyBatis, JDBC 등 다른 툴을 함께 사용할 경우에도 DB와 영속성 컨택스트가 달라지는 문제가 발생할 수 있음 -> 잘 초기화해줘야 함
        List<Member> result = memberRepository.findByUsername("member5");
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5);

        // bluk update 이후 영속성 컨텍스트 날려줘야 함
        // @Modifying에 clearAutomatically 옵션 사용!
//        em.flush();
//        em.clear();
        result = memberRepository.findByUsername("member5");
        member5 = result.get(0);
        System.out.println("member5 = " + member5);

        // then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() {
        // given

        // member1 -> teamA
        // member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // when

        // N + 1 문제 -> 1번 쿼리 한 후 N개의 결과가 나왔을 때, N개만큼 더 쿼리를 날려야하는 경우 -> 1 + N번의 쿼리로 네트워크를 타야해 성능이 낮음
//        List<Member> members = memberRepository.findAll();
//        List<Member> members = memberRepository.findMemberFetchJoin();
        List<Member> members = memberRepository.findEntityGraphByUsername("member1");

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.teamClass = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint() {
        // given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
//        Member findMember = memberRepository.findById(member1.getId()).get();
//        findMember.setUsername("member2");

        // 변경 감지 -> 원본 필요 (객체를 2개 관리하는 격) -> 메모리 많이 사용 -> 비효율 적
        // 변경 감지 과정도 비용 소모
        // 변경할 일 없을 경우엔 ReadOnly로 최적화! -> JPA의 HINT로 Hibernate의 기능 활용
//        em.flush();

        // 변경이 안 일어남!
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        em.flush();
    }

    @Test
    public void lock() {
        // given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        List<Member> member11 = memberRepository.findLockByUsername("member1");
    }
}