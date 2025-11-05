package org.opencms.publishing.repository;

import org.opencms.publishing.entity.PublishJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublishJobRepository extends JpaRepository<PublishJobEntity, String> {
    
    List<PublishJobEntity> findByFinishTimeBetween(Long startTime, Long endTime);
    
    @Query("SELECT p FROM PublishJobEntity p WHERE p.finishTime IS NOT NULL ORDER BY p.enqueueTime ASC")
    List<PublishJobEntity> findAllFinishedJobsOrderedByEnqueueTime();
}
