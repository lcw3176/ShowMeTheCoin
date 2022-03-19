package com.joebrooks.showmethecoin.repository.userConfig;

import com.joebrooks.showmethecoin.repository.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserConfigRepository extends JpaRepository<UserConfigEntity, Long> {

    Optional<UserConfigEntity> findByUserId(UserEntity user);
}
