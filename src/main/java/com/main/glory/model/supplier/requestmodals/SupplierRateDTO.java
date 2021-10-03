package com.main.glory.model.supplier.requestmodals;

import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.SupplierRate;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    Date updatedDate;

    public SupplierRateDTO(SupplierRate supplierRate) {
        this.id = supplierRate.getId();
        this.supplierId = supplierRate.getSupplier().getId();
        this.itemName = supplierRate.getItemName();
        this.itemType = supplierRate.getItemType();
        this.rate = supplierRate.getRate();
        this.discountedRate = supplierRate.getDiscountedRate();
        this.gstRate = supplierRate.getGstRate();
        this.userHeadId = supplierRate.getUserHeadId();
        this.createdDate = supplierRate.getCreatedDate();
        this.createdBy = supplierRate.getCreatedBy();
        this.updatedBy = supplierRate.getUpdatedBy();
        this.updatedDate = supplierRate.getUpdatedDate();
    }
}
