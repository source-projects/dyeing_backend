package com.main.glory.model.supplier;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddSupplierRate {
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

}
