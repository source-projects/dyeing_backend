package com.main.glory.model.dyeingSlip.responce;

import com.main.glory.model.shade.ShadeData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemListForDirectDyeing {
    Long itemId;
    Long supplierId;
    String itemName;
    String supplierName;
    Double qty;

    public ItemListForDirectDyeing(Long itemId,  String itemName,Long supplierId, String supplierName) {
        this.itemId = itemId;
        this.supplierId = supplierId;
        this.itemName = itemName;
        this.supplierName = supplierName;
    }

    public ItemListForDirectDyeing(ItemListForDirectDyeing item, ShadeData shadeData,Double totalBatchWt) {
        this.itemId = item.getItemId();
        this.supplierId = item.getSupplierId();
        this.itemName = item.getItemName();
        this.supplierName = item.getSupplierName();
        this.qty = (shadeData.getConcentration()*totalBatchWt)/100;
    }
}
