package com.joebrooks.showmethecoin.repository.trade;

import com.joebrooks.showmethecoin.repository.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TradeService {

    private final TradeRepository tradeRepository;

    public void addTradeLog(TradeEntity tradeEntity){
        tradeRepository.save(tradeEntity);
    }

    public Page<TradeEntity> getTradeLogs(UserEntity user, int pageNum){
        return tradeRepository.findByUserId(user, PageRequest.of(pageNum, 10, Sort.by("id").descending()));
    }

    public List<TradeEntity> getTradeLogs(UserEntity user, LocalDateTime start, LocalDateTime end){
        return tradeRepository.findAllByUserIdAndCreatedDateBetween(user, start, end);
    }
}
