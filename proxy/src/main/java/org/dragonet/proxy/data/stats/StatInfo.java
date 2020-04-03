/*
 * DragonProxy
 * Copyright (C) 2016-2020 Dragonet Foundation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You can view the LICENSE file for more details.
 *
 * https://github.com/DragonetMC/DragonProxy
 */
package org.dragonet.proxy.data.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
