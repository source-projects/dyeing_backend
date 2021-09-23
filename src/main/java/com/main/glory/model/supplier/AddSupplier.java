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

}
