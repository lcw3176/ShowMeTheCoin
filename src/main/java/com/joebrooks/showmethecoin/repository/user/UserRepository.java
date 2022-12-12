package com.joebrooks.showmethecoin.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUserIdAndUserPw(String id, String pw);

    Optional<UserEntity> findByUserId(String id);

}
