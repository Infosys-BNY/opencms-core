package org.opencms.content.repository;

import org.opencms.content.entity.ResourceLockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResourceLockRepository extends JpaRepository<ResourceLockEntity, Long> {
    
    Optional<ResourceLockEntity> findByResourcePath(String resourcePath);
    
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM ResourceLockEntity r WHERE r.resourcePath = :resourcePath")
    void deleteByResourcePath(String resourcePath);
}
