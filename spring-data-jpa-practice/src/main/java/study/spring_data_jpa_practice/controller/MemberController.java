package study.spring_data_jpa_practice.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.spring_data_jpa_practice.dto.MemberDto;
import study.spring_data_jpa_practice.entity.Member;
import study.spring_data_jpa_practice.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    /*
    *
    * 도메인 클래스 컨버터가 동작해 PK를 기준으로 Spring Data가 알아서 조회해줌
    * 실무에선 별로 권장되지 않음
    * 조금만 복잡해져도 사용할 수 없고, PK를 주고 받는 경우가 드물어서
    * 도메인 클래스 컨버터로 조회한 내용은 조회용으로만 사용해야함 (트랜잭션 범위가 없는 상황에서 조회한 거라)
    *
    */
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }

    /*
    *
    * localhost:8080/members?page=0&size=3&sort=id,desc
    * page, size, sort 조건을 주면 Controller에서 Parameter 값을 PageRequest 객체로 생성해서 Pageable에 injection 해줌
    * application.yml에 default page size, max page size 등을 global로 설정할 수 있음
    *
    * @PageableDefault로 특정 메소드에만 설정을 줄 수도 있음
    * global 설정보다 우선순위가 높음
    *
    * 페이징 정보가 둘 이상일 경우 접두사로 구분할 수 있음
    * @Qualifier("member") Pageable memberPageable, @Qualifier("order") Pageable orderPageable ...
    * localhost:8080/members?member_page=0&order_page=1
    *
    */
    @GetMapping("/members")
    public Page<Member> list(@PageableDefault(size = 5, sort = "username") Pageable pageable) {
        // 실무에선 Entity를 그대로 노출하면 안 됨,,,
        return memberRepository.findAll(pageable);
    }

    @GetMapping("/members2")
    public Page<MemberDto> list2(@PageableDefault(size = 5, sort = "username") Pageable pageable) {
//        return memberRepository.findAll(pageable)
//                .map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        return memberRepository.findAll(pageable).map(MemberDto::new);
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
