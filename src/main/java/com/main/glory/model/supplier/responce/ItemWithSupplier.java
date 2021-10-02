package com.main.glory.model.supplier.responce;


import com.main.glory.model.supplier.Supplier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class ItemWithSupplier {
    Long id;
    String itemName;
    Long supplierId;
    Double rate;
    Supplier supplier;


    public ItemWithSupplier(Long id, String itemName, Long supplierId, Double rate) {
        this.id = id;
        this.itemName = itemName;
        this.supplierId = supplierId;
        this.rate = rate;
    }

    public ItemWithSupplier(Long id, String itemName, Long supplierId, Double rate, Supplier supplier) {
        this.id = id;
        this.itemName = itemName;
        this.supplierId = supplierId;
        this.rate = rate;
        this.supplier = supplier;
    }
}
