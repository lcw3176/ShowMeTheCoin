package com.joebrooks.showmethecoin.repository.account;

import com.joebrooks.showmethecoin.repository.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccountEntity, Long> {
    UserAccountEntity findByUser(UserEntity userEntity);
}
