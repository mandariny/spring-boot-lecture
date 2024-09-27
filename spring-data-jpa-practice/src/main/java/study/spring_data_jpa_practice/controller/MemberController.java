package study.spring_data_jpa_practice.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
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

    @PostConstruct
    public void init() {
        memberRepository.save(new Member("memberA"));
    }
}
