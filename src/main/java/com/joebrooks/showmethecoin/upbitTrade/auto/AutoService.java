package com.joebrooks.showmethecoin.upbitTrade.auto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutoService {

    private final AutoTrade autoTrade;

    public void execute(AutoCommand command){
        autoTrade.setCommand(command);
    }
}
