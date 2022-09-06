package com.joebrooks.showmethecoin.repository.tradeinfo;

import com.joebrooks.showmethecoin.trade.upbit.CoinType;
import com.joebrooks.showmethecoin.repository.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TradeInfoServiceTest {

    @Autowired
    private TradeInfoService tradeInfoService;

    @Autowired
    private UserService userService;


//    @Test
//    void test(){
//        System.out.println(tradeInfoService.getTradeCoinsCount(userService.getUser("hello")));
//    }


    @Test
    void 같은코인의_여러_거래내역_중_최신_데이터만_가져오는지(){

        System.out.println(tradeInfoService.getRecentTrade(userService.getUser("hello"), CoinType.SAND).getOrderedAt());
    }

    @Test
    void 코인_종류만_가져오는지(){

        tradeInfoService.getAllTradeCoins(userService.getUser("hello")).forEach(i -> {
            System.out.println(i.getKoreanName());
        });
    }
}