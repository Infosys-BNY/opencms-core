package org.opencms.publishing.repository;

import org.opencms.publishing.entity.PublishHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublishHistoryRepository extends JpaRepository<PublishHistoryEntity, Integer> {
    
    List<PublishHistoryEntity> findByHistoryId(String historyId);
    
    @Modifying
    @Query("DELETE FROM PublishHistoryEntity p WHERE p.publishTag < :publishTag")
    void deleteByPublishTagLessThan(@Param("publishTag") Integer publishTag);
}
