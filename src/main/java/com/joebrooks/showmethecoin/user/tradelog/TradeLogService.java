package com.joebrooks.showmethecoin.user.tradelog;

import com.joebrooks.showmethecoin.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TradeLogService {

    private final TradeLogRepository tradeLogRepository;

    public void addTradeLog(TradeLogEntity tradeLogEntity){
        tradeLogRepository.save(tradeLogEntity);
    }

    public Page<TradeLogEntity> getTradeLogs(UserEntity user, int pageNum){
        return tradeLogRepository.findByUser(user, PageRequest.of(pageNum - 1, 10, Sort.by("orderEndDate").descending()));
    }

    public List<TradeLogEntity> getTradeLogs(UserEntity user, LocalDateTime start, LocalDateTime end){
        return tradeLogRepository.findAllByUserAndOrderEndDateBetween(user, start, end);
    }

    public long getTotalSize(UserEntity user){
        return tradeLogRepository.findAllByUser(user).size();
    }

    public List<TradeLogEntity> getLogsByTimeDesc(UserEntity user){
        return tradeLogRepository.findByUser(user, Sort.by(Sort.Direction.DESC, "orderEndDate"));
    }
}
