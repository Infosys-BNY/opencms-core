package org.opencms.config.repository;

import org.opencms.config.entity.LoginSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginSettingsRepository extends JpaRepository<LoginSettings, Long> {
}
