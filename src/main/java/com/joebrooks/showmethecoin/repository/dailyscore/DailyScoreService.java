package com.joebrooks.showmethecoin.repository.dailyscore;

import com.joebrooks.showmethecoin.repository.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyScoreService {

    private final DailyScoreRepository dailyScoreRepository;

    public void addScore(DailyScoreEntity dailyScore){
        dailyScoreRepository.save(dailyScore);
    }

    public List<DailyScoreEntity> getScore(UserEntity user){

        return dailyScoreRepository.findAllByUser(user);
    }


    public List<DailyScoreEntity> getTopThree(){
        return dailyScoreRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"))
                .stream()
                .filter(distinctByKey(i -> i.getUser().getUserId()))
                .sorted(Comparator.comparing(DailyScoreEntity::getTodayEarnPrice).reversed())
                .limit(3)
                .collect(Collectors.toList());

    }

    private <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
