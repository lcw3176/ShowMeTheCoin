package com.joebrooks.showmethecoin.trade.tradeinfo;

import com.joebrooks.showmethecoin.user.UserEntity;
import com.joebrooks.showmethecoin.trade.upbit.CoinType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TradeInfoRepository extends JpaRepository<TradeInfoEntity, Long> {

    Optional<TradeInfoEntity> findByUserIdAndCoinType(UserEntity userId, CoinType coinType);

    List<TradeInfoEntity> findAllByUser(UserEntity user);

    @Query("SELECT DISTINCT coinType FROM trade_info WHERE user = (:user)")
    List<CoinType> findDistinctCoinTypeByUser(@Param("user") UserEntity user);

    TradeInfoEntity findTopByUserAndCoinTypeOrderByOrderedAtDesc(UserEntity user, CoinType coinType);

    List<TradeInfoEntity> findAllByUserAndCoinType(UserEntity user, CoinType coinType);


    void removeAllByUserAndCoinType(UserEntity user, CoinType coinType);

    void removeByUuid(String uuid);
}
