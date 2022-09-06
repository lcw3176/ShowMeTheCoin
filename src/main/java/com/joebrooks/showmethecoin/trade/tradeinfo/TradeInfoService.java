package com.joebrooks.showmethecoin.trade.tradeinfo;

import com.joebrooks.showmethecoin.user.UserEntity;
import com.joebrooks.showmethecoin.trade.upbit.CoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TradeInfoService {

    private final TradeInfoRepository tradeInfoRepository;

    public void save(TradeInfoEntity tradeInfoEntity){
        tradeInfoRepository.save(tradeInfoEntity);
    }

    public void acceptOrder(UserEntity user, CoinType coinType){
        List<TradeInfoEntity> lst = tradeInfoRepository.findAllByUserAndCoinType(user, coinType);

        for(TradeInfoEntity i : lst){
            i.acceptOrder();
            tradeInfoRepository.save(i);
        }
    }

    public void orderComplete(UserEntity user, CoinType coinType){
        List<TradeInfoEntity> lst = tradeInfoRepository.findAllByUserAndCoinType(user, coinType);

        for(TradeInfoEntity i : lst){
            i.complete();
            tradeInfoRepository.save(i);
        }
    }



    public void orderCanceled(UserEntity user, CoinType coinType){
        List<TradeInfoEntity> lst = tradeInfoRepository.findAllByUserAndCoinType(user, coinType);

        for(TradeInfoEntity i : lst){
            i.cancelOrder();
            tradeInfoRepository.save(i);
        }
    }

    public void removeTradeLogs(UserEntity user, CoinType coinType){
        tradeInfoRepository.removeAllByUserAndCoinType(user, coinType);
    }

    public void removeOrder(String uuid){
        tradeInfoRepository.removeByUuid(uuid);
    }

    public List<TradeInfoEntity> getAllTrades(UserEntity user){
        return tradeInfoRepository.findAllByUser(user);
    }

    public List<CoinType> getAllTradeCoins(UserEntity user){
        return tradeInfoRepository.findDistinctCoinTypeByUser(user);
    }


    public TradeInfoEntity getRecentTrade(UserEntity user, CoinType coinType){
        return tradeInfoRepository.findTopByUserAndCoinTypeOrderByOrderedAtDesc(user, coinType);
    }

    public List<TradeInfoEntity> getTradeLogs(UserEntity user, CoinType coinType){
        return tradeInfoRepository.findAllByUserAndCoinType(user, coinType);
    }

    public int getTradeCoinsCount(UserEntity user){
        return tradeInfoRepository.countDistinctCoinTypeByUser(user);
    }

}
