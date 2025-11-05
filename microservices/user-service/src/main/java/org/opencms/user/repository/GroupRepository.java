package org.opencms.user.repository;

import org.opencms.user.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {
    Optional<Group> findByGroupName(String groupName);
    boolean existsByGroupName(String groupName);
}
