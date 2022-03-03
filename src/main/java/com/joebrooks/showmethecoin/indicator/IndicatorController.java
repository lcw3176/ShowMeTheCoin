package com.joebrooks.showmethecoin.indicator;

import com.joebrooks.showmethecoin.global.upbit.CoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/indicator")
public class IndicatorController {

    private final IndicatorService indicatorService;


    @GetMapping("/{coinType}")
    public ResponseEntity<List<Indicator>> getIndicator(@RequestParam("type") Optional<String> indicators,
                                                       @PathVariable("coinType") Optional<String> coinType) {
        indicators.orElseThrow(() -> {
            throw new RuntimeException("no indicator " + indicators);
        });


        coinType.orElseThrow(() -> {
            throw new RuntimeException("no coinType " + coinType);
        });


        return ResponseEntity.ok(indicatorService.execute(indicators.get(),
                CoinType.valueOf(coinType.get().toUpperCase())));
    }
}
