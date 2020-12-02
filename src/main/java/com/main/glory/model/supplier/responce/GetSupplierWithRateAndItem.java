package com.main.glory.model.supplier.responce;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class GetSupplierWithRateAndItem {
    Long id;
    String name;

    List<RateAndItem> rateAndItemList;
}
