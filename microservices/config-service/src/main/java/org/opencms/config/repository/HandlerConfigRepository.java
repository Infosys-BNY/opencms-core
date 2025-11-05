package org.opencms.config.repository;

import org.opencms.config.entity.HandlerConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandlerConfigRepository extends JpaRepository<HandlerConfig, Long> {
}
