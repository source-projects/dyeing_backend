package com.main.glory.model.supplier.responce;

import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.SupplierRate;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SupplierRateDTO {
    Long id;
    Long supplierId;
    String itemName;
    String itemType;
    Double rate;
    Double discountedRate;
    Double gstRate;
    Long userHeadId;
    Date createdDate;
    Long createdBy;
    Long updatedBy;

    public SupplierRateDTO(SupplierRate supplierRate) {
        this.id = supplierRate.getId();
        this.supplierId = supplierRate.getSupplier().getId();
        this.itemName = supplierRate.getItemName();
        this.itemType = supplierRate.getItemType();
        this.rate = supplierRate.getRate();
        this.discountedRate = supplierRate.getDiscountedRate();
        this.gstRate = supplierRate.getGstRate();
        this.userHeadId = null;
        this.createdDate = supplierRate.getCreatedDate();
        this.createdBy = null;
        this.updatedBy = null;
    }
}
