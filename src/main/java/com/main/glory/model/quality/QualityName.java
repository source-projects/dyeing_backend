package com.main.glory.model.quality;

import com.main.glory.model.quality.request.AddQualityName;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.SupplierRate;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class QualityName {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String qualityName;//should be unique
    Long createdBy;
    Date createdDate;
    Long updatedBy;
    Date updatedDate;
    @ColumnDefault("0")
    Double rate;

    public QualityName(AddQualityName qualityName) {
        this.id = qualityName.getId();
        this.qualityName = qualityName.getQualityName();
        this.createdBy = qualityName.getCreatedBy();
        this.updatedBy = qualityName.getUpdatedBy();
        this.rate = qualityName.getRate();
        this.createdDate = qualityName.getCreatedDate();
        this.updatedDate = qualityName.getUpdatedDate();
    }

    @PrePersist
    public void create()
    {
        this.createdDate = new Date(System.currentTimeMillis());
    }
    @PreUpdate
    public void update()
    {
        this.updatedDate = new Date(System.currentTimeMillis());
    }

}
