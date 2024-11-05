package study.spring_data_jpa_practice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.spring_data_jpa_practice.entity.Item;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {
    @Autowired ItemRepository itemRepository;

    @Test
    public void save() {
        /*
        *
        * Id값이 존재하므로 save()에서 isNew가 false가 됨 -> persist()가 아닌 merge() 호출
        * merge()는 select 후에 결과가 없으면 새로운 entity로 판단 -> 비효율적
        * Entity에 Persistable<T>을 implement해서 새로운 엔티티 여부를 직접 구혀하는게 효과적
        *
        */
        Item item = new Item("A");
        itemRepository.save(item);
    }
}