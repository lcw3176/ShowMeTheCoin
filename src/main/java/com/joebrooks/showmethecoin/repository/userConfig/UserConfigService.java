package com.joebrooks.showmethecoin.repository.userConfig;

import com.joebrooks.showmethecoin.repository.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserConfigService {

    private final UserConfigRepository userConfigRepository;

    public List<UserConfigEntity> getAllUserConfig(){
        return userConfigRepository.findAll();
    }

    public Optional<UserConfigEntity> getUserConfig(UserEntity id) {
        return userConfigRepository.findByUserId(id);
    }


    public void save(UserConfigEntity userConfig){
        userConfigRepository.save(userConfig);
    }

    public void stopAllUsersTrade(){
        userConfigRepository.findAll().forEach(i -> {
            i.changeTradeStatus(false);

            userConfigRepository.save(i);
        });
    }

    public void initAllUsersTradeLevel(){
        userConfigRepository.findAll().forEach(i -> {
            i.changeDifferenceLevel(0);

            userConfigRepository.save(i);
        });
    }
}
