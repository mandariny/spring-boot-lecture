package study.spring_data_jpa_practice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.spring_data_jpa_practice.dto.MemberDto;
import study.spring_data_jpa_practice.entity.Member;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /*
    *
    * 쿼리 메소드 기능
    * 1. 메소드 이름으로 쿼리 생성
    *   - spring.io -> project -> spring data -> spring data jpa -> learn -> reference doc -> Query Creation 참고
    *   - 조건이 2개 이하일 경우 사용하기 좋음
    * 2. NamedQuery
    *   - 실무에선 거의 사용 안 함
    *   - 엔티티에 네임드 쿼리를 작성해놔야 함
    *   - 오류(오타 등)를 컴파일 시점에 발견하기 쉬움
    * 3. @Query
    *   - 실무에서 많이 사용됨
    *   - JPQL을 바로 사용해 복잡한 문제 해결 가능
    *   - 오류(오타 등)를 컴파일 시점에 발견하기 쉬움
    *   - 이름이 없는 NamedQuery라고 생각
    */
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // NamedQuery
//    @Query(name = "Member.findByUsername")
    // Member.class 내에 네임드 쿼리가 있는지 확인 후 없으면 메소드 이름으로 쿼리 생성
//    List<Member> findByUsername(@Param("username") String username);
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // new operation. 마치 새로 객체를 생성하듯 constructor에 들어갈 내용을 다 써줘야 함
    @Query("select new study.spring_data_jpa_practice.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // 리스트로 In 사용 가능
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    // 반환 타입 아무거나 가능!
    // List로 조회 시 결과가 없으면 Empty List 반환
    // 단건 조회 시 결과가 없으면 null 반환 -> Exception 안 뜸! 순수 JPA와 다른 점
    // 자바8 이후엔 그냥 Optional 사용~
    // 단건 조회 시 결과가 2개 이상이면 Optional이든 아니든 Exception 발생 -> Spring Framework의 Exception으로 변환해서 반환해줌
    List<Member> findListByUsername(String username);
    Member findMemberByUsername(String username);
    Optional<Member> findOptionalByUsername(String username);

    // 카운트 쿼리 분리
    // join 등으로 count 쿼리 성능이 낮아질 때 사용
//    @Query(value = "select m from Member m left join m.team t",
//            countQuery = "select count(m.username) from Member m")
    @Query(value = "select m from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
//    Slice<Member> findByAge(int age, Pageable pageable);
//    List<Member> findByAge(int age, Pageable pageable);
}

