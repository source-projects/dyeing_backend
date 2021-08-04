package com.main.glory.model.dispatch.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class UnitDetail {
    String billingUnit;
    String inwardUnit;
    Double wtPer100m;

    public UnitDetail(String billingUnit, String inwardUnit, Double wtPer100m) {
        this.billingUnit = billingUnit;
        this.inwardUnit = inwardUnit;
        this.wtPer100m = wtPer100m;
    }
}
