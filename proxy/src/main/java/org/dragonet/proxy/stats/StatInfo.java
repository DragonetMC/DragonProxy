package org.dragonet.proxy.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

@AllArgsConstructor
public class StatInfo {
    @Getter
    private String name;

    @Getter
    private StatMeasurement measurement;

    private final DecimalFormat decimalFormat = new DecimalFormat("########0.00");
    private final NumberFormat numberFormat = NumberFormat.getInstance(Locale.UK);

    public String format(int number) {
        switch(measurement) {
            case NONE:
                return numberFormat.format(number);
            case TIME:
                double d0 = (double) number / 20.0D;
                double d1 = d0 / 60.0D;
                double d2 = d1 / 60.0D;
                double d3 = d2 / 24.0D;
                double d4 = d3 / 365.0D;

                return d4 > 0.5D ? decimalFormat.format(d4) + " y" : (d3 > 0.5D ? decimalFormat.format(d3) + " d" : (d2 > 0.5D ? decimalFormat.format(d2) + " h" : (d1 > 0.5D ?     decimalFormat.format(d1) + " m" : d0 + " s")));
            case DISTANCE:
                double d5 = (double)number / 100.0D;
                double d6 = d5 / 1000.0D;

                return d6 > 0.5D ? decimalFormat.format(d6) + " km" : (d5 > 0.5D ? decimalFormat.format(d5) + " m" : number + " cm");
            case DIVIDE_BY_TEN:
                return decimalFormat.format((double) number * 0.1D);
        }
        return numberFormat.format(number);
    }
}
