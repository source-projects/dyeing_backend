package com.main.glory.model.supplier.responce;

import java.util.Date;

import com.main.glory.model.supplier.Supplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetSupplierPaginatedData {
    Long id;
    String supplierName;
    Double discountPercentage;
    Double gstPercentage;
    String remark;
    Long paymentTerms;
	String qualityName;
    public GetSupplierPaginatedData(Supplier supplier){
        this.id=supplier.getId();
        this.supplierName=supplier.getSupplierName();
        this.discountPercentage=supplier.getDiscountPercentage();
        this.gstPercentage=supplier.getGstPercentage();
        this.qualityName=supplier.getQualityName()==null?null:supplier.getQualityName().getQualityName();
        this.remark=supplier.getRemark();
        this.paymentTerms=supplier.getPaymentTerms();
    }
}
