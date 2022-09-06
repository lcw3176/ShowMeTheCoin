package com.joebrooks.showmethecoin.user.userconfig;

import com.joebrooks.showmethecoin.user.UserEntity;
import com.joebrooks.showmethecoin.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserConfigService {

    private final UserConfigRepository userConfigRepository;
    private final UserService userService;

    public List<UserConfigEntity> getAllUserConfig(){
        return userConfigRepository.findAll();
    }

    public UserConfigEntity getUserConfig(UserEntity id) {
        return userConfigRepository.findByUser(id).orElseThrow(() -> {
            throw new RuntimeException("유저 설정 없음");
        });
    }

    public UserConfigEntity getUserConfig(String userId) {
        return userConfigRepository.findByUser(userService.getUser(userId)).orElseThrow(() -> {
            throw new RuntimeException("유저 설정 없음");
        });
    }


    public void startTrading(UserConfigEntity userConfig){
        userConfig.startTrading();
        userConfigRepository.save(userConfig);
    }

    public void stopTrading(UserConfigEntity userConfig){
        userConfig.stopTrading();
        userConfigRepository.save(userConfig);
    }

    public void changeConfig(UserEntity user, UserConfigEntity userConfig){
        UserConfigEntity userConfigEntity = userConfigRepository.findByUser(user).orElseThrow(() -> {
            throw new RuntimeException("유저 설정 없음");
        });

        userConfigEntity.changeStrategy(userConfig.getStrategy());
        userConfigEntity.changeMaxBetCount(userConfig.getMaxBetCount());
        userConfigEntity.changeMaxTradeCoinCount(userConfig.getMaxTradeCoinCount());
        userConfigEntity.changeOrderCancelMinute(userConfig.getOrderCancelMinute());
        userConfigEntity.changeCandleMinute(userConfig.getCandleMinute());
        userConfigEntity.setAllowSellWithLoss(userConfig.isAllowSellWithLoss());
        userConfigEntity.changeCashDividedCount(userConfig.getCashDividedCount());

        userConfigRepository.save(userConfigEntity);
    }


    public void save(UserConfigEntity userConfig){
        userConfigRepository.save(userConfig);
    }


}
