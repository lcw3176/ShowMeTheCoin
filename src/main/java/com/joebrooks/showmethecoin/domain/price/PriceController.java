package com.joebrooks.showmethecoin.domain.price;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/price")
public class PriceController {

    private final PriceService priceService;

    @GetMapping
    public ResponseEntity<List<Price>> getPrice(){
        return ResponseEntity.of(priceService.getPrices(Day.TWO));
    }
}
