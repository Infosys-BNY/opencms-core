package org.opencms.config.repository;

import org.opencms.config.entity.MailHost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailHostRepository extends JpaRepository<MailHost, Long> {
}
