package org.opencms.content.repository;

import org.opencms.content.entity.OfflineResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfflineResourceRepository extends JpaRepository<OfflineResourceEntity, String> {
}
