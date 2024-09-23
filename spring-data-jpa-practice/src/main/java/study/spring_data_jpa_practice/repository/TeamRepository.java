package study.spring_data_jpa_practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.spring_data_jpa_practice.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
