package com.main.glory.model.color.request;

import com.main.glory.model.color.ColorBox;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.SupplierRate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllBox extends ColorBox {
    Long id;
    String name;
    Long itemId;
    String itemname;


    public GetAllBox(ColorBox colorBox, Long id, String name, Long itemId, String itemname) {
        super(colorBox);
        this.id = id;
        this.name = name;
        this.itemId = itemId;
        this.itemname = itemname;
    }

    public GetAllBox(ColorBox colorBox, SupplierRate supplierRate, Supplier supplier) {
        super(colorBox);
        this.id=supplier.getId();
        this.name=supplier.getSupplierName();
        this.itemId=supplierRate.getId();
        this.itemname=supplierRate.getItemName();
    }
}
