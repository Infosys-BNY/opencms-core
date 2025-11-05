package org.opencms.config.repository;

import org.opencms.config.entity.CacheSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CacheSettingsRepository extends JpaRepository<CacheSettings, Long> {
}
