package com.joebrooks.showmethecoin.user.account;

import com.joebrooks.showmethecoin.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccountEntity, Long> {
    UserAccountEntity findByUser(UserEntity userEntity);
}
