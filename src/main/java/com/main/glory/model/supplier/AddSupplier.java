package com.main.glory.model.supplier;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.user.UserData;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AddSupplier {
    Long id;
    String supplierName;
    Double discountPercentage;
    Double gstPercentage;
    String remark;
    Date createdDate;
    Date updatedDate;
    Long paymentTerms;
	Long qualityNameId;
    private Long createdBy;
    private Long updatedBy;
    private UserData userHeadId;
    List<Long> supplierRateIds;


    public AddSupplier(Supplier s) {
        this.id = s.getId();
        this.supplierName = s.getSupplierName();
        this.discountPercentage = s.getDiscountPercentage();
        this.gstPercentage = s.getGstPercentage();
        this.remark = s.getRemark();
        this.createdDate = s.getCreatedDate();
        this.updatedDate = s.getUpdatedDate();
        this.paymentTerms = s.getPaymentTerms();
        this.qualityNameId = s.getQualityName().getId();
        this.createdBy = s.getCreatedBy().getId();
        this.updatedBy = s.getUpdatedBy()==null?null:s.getUpdatedBy().getId();
        this.userHeadId = null;
        this.supplierRateIds = supplierRateIds;
    }
}
