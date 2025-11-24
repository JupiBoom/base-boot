package zz.hujing.baseboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zz.hujing.baseboot.domain.alert.AlertRecord;

import java.util.List;

@Repository
public interface AlertRecordRepository extends JpaRepository<AlertRecord, Long> {
    List<AlertRecord> findByProductIdOrderByCreatedTimeDesc(Long productId);
    List<AlertRecord> findByIsHandledFalseOrderByCreatedTimeDesc();
    List<AlertRecord> findByAlertLevelAndIsHandledFalseOrderByCreatedTimeDesc(String alertLevel);
}