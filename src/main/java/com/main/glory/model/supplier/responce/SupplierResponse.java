package com.main.glory.model.supplier.responce;

import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.SupplierRate;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    List<SupplierRateDTO> supplierRates;


    public SupplierResponse(Supplier s)
    {
        this.id= s.getId();
        this.supplierName = s.getSupplierName();
        this.discountPercentage =s.getDiscountPercentage();
        this.gstPercentage=s.getGstPercentage();
        this.remark = s.getRemark();
        this.createdBy = s.getCreatedBy()==null?null:s.getCreatedBy().getId();
        this.createdDate = s.getCreatedDate();
        this.updatedDate = s.getUpdatedDate();
        this.paymentTerms= s.getPaymentTerms();
        this.updatedBy = s.getUpdatedBy()==null?null:s.getUpdatedBy().getId();
        this.userHeadId =s.getUserHeadData()==null?null: s.getUserHeadData().getId();
        this.qualityNameId = s.getQualityName().getId();
        //this.supplierRateList = new ArrayList<>()s.getSupplierRates();

    }


}
