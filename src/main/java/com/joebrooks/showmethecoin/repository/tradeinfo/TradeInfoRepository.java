package com.joebrooks.showmethecoin.repository.tradeinfo;

import com.joebrooks.showmethecoin.repository.user.UserEntity;
import com.joebrooks.showmethecoin.trade.upbit.CoinType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TradeInfoRepository extends JpaRepository<TradeInfoEntity, Long> {

    Optional<TradeInfoEntity> findByUserIdAndCoinType(UserEntity userId, CoinType coinType);

    List<TradeInfoEntity> findAllByUserId(UserEntity user);

    List<TradeInfoEntity> findAllByUserIdAndCoinType(UserEntity user, CoinType coinType);

    int countDistinctCoinTypeByUserId(UserEntity user);
    void removeAllByUserIdAndCoinType(UserEntity user, CoinType coinType);

    void removeByUuid(String uuid);
}
