package com.main.glory.model.supplier.responce;

import com.main.glory.model.supplier.Supplier;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SupplierResponse {

    Long id;
    String supplierName;
    Double discountPercentage;
    Double gstPercentage;
    String remark;
    Long createdBy;
    @ApiModelProperty(hidden = true)
    Date createdDate;
    @ApiModelProperty(hidden = true)
    Date updatedDate;
    Long paymentTerms;
    Long updatedBy;
    Long userHeadId;
    Long qualityNameId;


    public SupplierResponse(Supplier s)
    {
        this.id= s.getId();
        this.supplierName = s.getSupplierName();
        this.discountPercentage =s.getDiscountPercentage();
        this.gstPercentage=s.getGstPercentage();
        this.remark = s.getRemark();
        this.createdBy = s.getCreatedBy();
        this.createdDate = s.getCreatedDate();
        this.updatedDate = s.getUpdatedDate();
        this.paymentTerms= s.getPaymentTerms();
        this.updatedBy = s.getUpdatedBy();
        this.userHeadId = s.getUserHeadId();
        this.qualityNameId = s.getQualityNameId();

    }


}
