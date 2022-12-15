package com.joebrooks.showmethecoin.exchange;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.annotation.PostConstruct;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExchangeUtil {

    private final NumberFormat numberFormat = new DecimalFormat("###,###.#####");

    @PostConstruct
    private void init() {
        numberFormat.setGroupingUsed(false);
    }

    public String priceFormatter(double price) {
        String numbers = numberFormat.format(price);

        if (!numbers.contains("\\.")) {
            return numbers;
        }

        String[] temp = numberFormat.format(price).split("\\.");
        String number = temp[0];
        String point = temp[1];

        if (Long.parseLong(point) <= 0) {
            return number;
        } else {

            if (Long.parseLong(number) <= 0) {
                return "0." + point;
            } else {
                return number + "." + point;
            }
        }
    }
}
