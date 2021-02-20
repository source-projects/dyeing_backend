package com.main.glory.model.supplier.responce;

import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.SupplierRate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class GetItemWithSupplier {
    Long supplierId;
    String supplierName;
    Long itemId;
    String itemName;
    String itemType;

    public GetItemWithSupplier(Long supplierId, String supplierName, Long itemId, String itemName) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.itemId = itemId;
        this.itemName = itemName;
    }

    public GetItemWithSupplier(Supplier supplier, ItemWithSupplier item) {
        this.supplierId=supplier.getId();
        this.supplierName=supplier.getSupplierName();
        this.itemId=item.id;
        this.itemName=item.itemName;
    }

    public GetItemWithSupplier(Supplier supplier, ItemWithSupplier item, SupplierRate supplierRate) {
        this.supplierId=supplier.getId();
        this.supplierName=supplier.getSupplierName();
        this.itemId=item.id;
        this.itemName=item.itemName;
        this.itemType = supplierRate.getItemType();
    }
}
