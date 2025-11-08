package org.opencms.content.repository;

import org.opencms.content.entity.OfflineStructureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfflineStructureRepository extends JpaRepository<OfflineStructureEntity, String> {
    
    Optional<OfflineStructureEntity> findByResourcePath(String resourcePath);
    
    @Query("SELECT s FROM OfflineStructureEntity s WHERE s.structureId = :structureId")
    Optional<OfflineStructureEntity> findByStructureId(String structureId);
}
