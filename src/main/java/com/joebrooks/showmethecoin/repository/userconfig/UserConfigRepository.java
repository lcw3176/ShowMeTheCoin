package com.joebrooks.showmethecoin.repository.userconfig;

import com.joebrooks.showmethecoin.repository.user.UserEntity;
import com.joebrooks.showmethecoin.trade.strategy.StrategyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserConfigRepository extends JpaRepository<UserConfigEntity, Long> {

    Optional<UserConfigEntity> findByUser(UserEntity user);

    List<UserConfigEntity> findAllByStrategy(StrategyType strategyType);

    @Query("SELECT DISTINCT strategy FROM user_config")
    List<StrategyType> findDistinctStrategy();
}
