package study.spring_data_jpa_practice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
// 진짜 상속 관계가 아니라 속성만 내려서 사용
@MappedSuperclass
public class JpaBaseEntity {
    // 수정될 수 없는 값
    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;

    // Persist 전 이벤트 발생
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updateDate = now;
    }

    // Update 전 이벤트 발생
    @PreUpdate
    public void preUpdate() {
        updateDate = LocalDateTime.now();
    }
}
