package com.main.glory.model.color.responsemodals;

import com.main.glory.model.color.request.ItemWithLeftQty;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.SupplierRate;
import com.main.glory.model.supplier.responce.GetItemWithSupplier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SupplierItemWithLeftColorQty {
    Long supplierId;
    String name;
    Long itemId;
    String itemName;
    Double leftQty;
    Double packedQty;

    public SupplierItemWithLeftColorQty(ItemWithLeftQty itemWithLeftQty, GetItemWithSupplier obj) {
        this.supplierId=obj.getSupplierId();
        this.name=obj.getSupplierName();
        this.itemId=itemWithLeftQty.getItemId();
        this.itemName=obj.getItemName();
        this.leftQty=itemWithLeftQty.getAvailableQty();
    }

    public SupplierItemWithLeftColorQty(ItemWithLeftQty itemWithLeftQty, Supplier supplier, SupplierRate supplierRate) {
        this.supplierId=supplier.getId();
        this.name=supplier.getSupplierName();
        this.itemId=itemWithLeftQty.getItemId();
        this.itemName=supplierRate.getItemName();
        this.leftQty=itemWithLeftQty.getAvailableQty();
    }

    public SupplierItemWithLeftColorQty(Supplier supplier, SupplierRate supplierRate) {
        this.supplierId=supplier.getId();
        this.name=supplier.getSupplierName();
        this.itemId=supplierRate.getId();
        this.itemName=supplierRate.getItemName();
    }

    public SupplierItemWithLeftColorQty(Supplier supplier, SupplierRate supplierRate, Double leftQty) {
        this.supplierId=supplier.getId();
        this.name=supplier.getSupplierName();
        this.itemId=supplierRate.getId();
        this.itemName=supplierRate.getItemName();
        this.leftQty=leftQty;
    }

    public SupplierItemWithLeftColorQty(Supplier supplier, SupplierRate supplierRate, Double leftQty, Double packedQty) {
        this.supplierId=supplier.getId();
        this.name=supplier.getSupplierName();
        this.itemId=supplierRate.getId();
        this.itemName=supplierRate.getItemName();
        this.leftQty=leftQty;
        this.packedQty=packedQty;
    }
}
