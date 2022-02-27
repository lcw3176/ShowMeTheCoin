package com.joebrooks.showmethecoin.auto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutoService {

    private final AutoRoutine autoRoutine;

    public void execute(CommandRequest request){
        autoRoutine.setCommand(request);
    }
}
