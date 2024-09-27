package study.spring_data_jpa_practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class SpringDataJpaPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDataJpaPracticeApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		// 인터페이스 내 메소드가 하나일 경우 람다로 바꿀 수 있음
		return () -> Optional.of(UUID.randomUUID().toString());
		/*
		 *	SpringSecurity Context에서 Session 정보 가져와 ID 꺼냄
		 *	HTTP Session에서 ID를 꺼내 사용할 수도 있음
		 *
		 */
	}
}
