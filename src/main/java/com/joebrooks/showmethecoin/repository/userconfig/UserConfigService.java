package com.joebrooks.showmethecoin.repository.userconfig;

import com.joebrooks.showmethecoin.repository.user.UserEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserConfigService {

    private final UserConfigRepository userConfigRepository;

    public List<UserConfigEntity> getAllUserConfig(){
        return userConfigRepository.findAll();
    }

    public UserConfigEntity getUserConfig(UserEntity id) {
        return userConfigRepository.findByUser(id).orElseThrow(() -> {
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



    public void save(UserConfigEntity userConfig){
        userConfigRepository.save(userConfig);
    }


}
