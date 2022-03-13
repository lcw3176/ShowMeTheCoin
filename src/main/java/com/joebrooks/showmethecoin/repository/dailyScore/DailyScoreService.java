package com.joebrooks.showmethecoin.repository.dailyScore;

import com.joebrooks.showmethecoin.repository.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyScoreService {

    private final DailyScoreRepository dailyScoreRepository;

    public void addScore(DailyScoreEntity dailyScore){
        dailyScoreRepository.save(dailyScore);
    }

    public List<DailyScoreEntity> getScore(UserEntity user){

        return dailyScoreRepository.findAllByUserId(user);
    }
}
