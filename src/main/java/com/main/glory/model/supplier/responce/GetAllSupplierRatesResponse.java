package com.main.glory.model.supplier.responce;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllSupplierRatesResponse {
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
    String supplierName;
}
