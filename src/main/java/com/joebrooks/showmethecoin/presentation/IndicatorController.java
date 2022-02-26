package com.joebrooks.showmethecoin.presentation;

import com.joebrooks.showmethecoin.application.indicator.IndicatorService;
import com.joebrooks.showmethecoin.infra.upbit.coin.CoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/indicator")
public class IndicatorController {

    private final IndicatorService indicatorService;

    @GetMapping("/{detail}/{coinType}")
    public ResponseEntity getRsi(@PathVariable("detail") Optional<String> detail,
                                 @PathVariable("coinType") Optional<String> coinType) {
        detail.orElseThrow(() -> {
            throw new RuntimeException("no indicator " + detail);
        });

        coinType.orElseThrow(() -> {
            throw new RuntimeException("no coinType " + coinType);
        });


        return ResponseEntity.ok(indicatorService.execute(detail.get(), CoinType.valueOf(coinType.get().toUpperCase())));
    }

}
