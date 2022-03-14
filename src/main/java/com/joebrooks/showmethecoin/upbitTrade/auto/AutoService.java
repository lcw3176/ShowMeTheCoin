package com.joebrooks.showmethecoin.upbitTrade.auto;

import com.joebrooks.showmethecoin.repository.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutoService {

    private final AutoRoutine autoRoutine;

    public void execute(AutoCommand command, UserEntity user){
        autoRoutine.setCommand(command, user);
    }
}
