package org.opencms.user.repository;

import org.opencms.user.entity.UserData;
import org.opencms.user.entity.UserDataId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, UserDataId> {
    List<UserData> findByUserId(String userId);
}
