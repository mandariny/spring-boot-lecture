package study.spring_data_jpa_practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.spring_data_jpa_practice.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
