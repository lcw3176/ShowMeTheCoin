package com.joebrooks.showmethecoin.repository.userconfig;

import com.joebrooks.showmethecoin.trade.strategy.StrategyType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserConfigServiceTest {

    @Autowired
    private UserConfigService userConfigService;


    @Test
    void 같은_전략인_유저들만_가져오기(){
        userConfigService.getSameStrategyUsers(StrategyType.SHORT).forEach(i -> {
            assertEquals(StrategyType.SHORT, i.getStrategy());
        });
    }
}