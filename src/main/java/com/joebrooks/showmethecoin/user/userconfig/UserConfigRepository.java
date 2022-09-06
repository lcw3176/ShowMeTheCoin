package com.joebrooks.showmethecoin.user.userconfig;

import com.joebrooks.showmethecoin.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserConfigRepository extends JpaRepository<UserConfigEntity, Long> {

    Optional<UserConfigEntity> findByUser(UserEntity user);

}
