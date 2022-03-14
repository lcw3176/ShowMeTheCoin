package com.joebrooks.showmethecoin.upbitTrade.indicator;

//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/indicator")
//public class IndicatorController {
//
//    private final IndicatorService indicatorService;
//
//
//    @GetMapping("/{coinType}")
//    public ResponseEntity<List<Indicator>> getIndicator(@RequestParam("type") Optional<String> indicators,
//                                                       @PathVariable("coinType") Optional<String> coinType) {
//        indicators.orElseThrow(() -> {
//            throw new RuntimeException("no indicator " + indicators);
//        });
//
//
//        coinType.orElseThrow(() -> {
//            throw new RuntimeException("no coinType " + coinType);
//        });
//
//
//        return ResponseEntity.ok(indicatorService.execute(indicators.get(),
//                CoinType.valueOf(coinType.get().toUpperCase())));
//    }
//}
