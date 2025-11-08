package org.opencms.content.repository;

import org.opencms.content.entity.OfflineContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfflineContentRepository extends JpaRepository<OfflineContentEntity, String> {
}
