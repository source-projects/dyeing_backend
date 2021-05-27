package com.main.glory.model.quality.request;

import com.main.glory.model.quality.QualityName;
import com.main.glory.model.supplier.Supplier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddQualityName {

    Long id;
    String qualityName;//should be unique
    Long createdBy;
    Date createdDate;
    Long updatedBy;
    Date updatedDate;
    Double rate;
    List<Supplier> supplierList;

    public AddQualityName(QualityName qualityName,List<Supplier> supplierList)
    {
        this.id = qualityName.getId();
        this.qualityName = qualityName.getQualityName();
        this.createdBy = qualityName.getCreatedBy();
        this.createdDate = qualityName.getCreatedDate();
        this.updatedBy = qualityName.getUpdatedBy();
        this.updatedDate = qualityName.getUpdatedDate();
        this.rate = qualityName.getRate();
        this.supplierList = supplierList;
    }

}
