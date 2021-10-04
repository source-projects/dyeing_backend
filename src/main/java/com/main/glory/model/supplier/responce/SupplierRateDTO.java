package com.main.glory.model.supplier.responce;

import com.main.glory.model.supplier.Supplier;
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

}
