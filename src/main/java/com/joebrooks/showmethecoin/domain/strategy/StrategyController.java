package com.joebrooks.showmethecoin.domain.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/strategy")
public class StrategyController {

    private final StrategyManager strategyManager;

    @GetMapping("/{detail}")
    public ResponseEntity getRsi(@PathVariable("detail") String detail) {
        Optional.ofNullable(detail).orElseThrow(() -> {
            throw new RuntimeException("no strategy");
        });



        return ResponseEntity.ok(strategyManager.execute(detail));
    }

}
