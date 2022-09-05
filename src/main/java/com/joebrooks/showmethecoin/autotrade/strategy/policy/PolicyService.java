package com.joebrooks.showmethecoin.autotrade.strategy.policy;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final ApplicationContext applicationContext;

    public List<IBuyPolicy> getBuyPolicy(Class... clazz){
        return Arrays.stream(clazz)
                .map(i -> (IBuyPolicy) applicationContext.getBean(i))
                .collect(Collectors.toList());
    }

    public List<ISellPolicy> getSellPolicy(Class... clazz){
        return Arrays.stream(clazz)
                .map(i -> (ISellPolicy) applicationContext.getBean(i))
                .collect(Collectors.toList());
    }
}
