package com.main.glory.model.supplier.responce;

import com.main.glory.model.supplier.SupplierRate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RateAndItem {

    Long id;
    String itemName;
    Double rate;

    public RateAndItem(SupplierRate other)
    {
        this.id=other.getId();
        this.itemName=other.getItemName();
        this.rate=other.getGstRate();
    }
}
