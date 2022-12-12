package com.joebrooks.showmethecoin.repository.userconfig;

import com.joebrooks.showmethecoin.repository.user.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConfigRepository extends JpaRepository<UserConfigEntity, Long> {

    Optional<UserConfigEntity> findByUser(UserEntity user);

}
