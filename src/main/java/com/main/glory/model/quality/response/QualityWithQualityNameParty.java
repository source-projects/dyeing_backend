package com.main.glory.model.quality.response;

import com.main.glory.model.quality.Quality;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QualityWithQualityNameParty {

    private Long id;
    private String  qualityId;
    String qualityName;
    private String qualityType;
    private String unit;
    private Long  partyId;
    private Double wtPer100m;
    private Double mtrPerKg;
    private String remark;
    private Date createdDate;
    private Long createdBy;
    private Long updatedBy;
    private Date updatedDate;
    private Date qualityDate;
    private Long userHeadId;
    private Double rate;
    private String HSN;
    Long qualityNameId;
    String billingUnit;

    public QualityWithQualityNameParty(Quality other,String qualityName) {
        this.id = other.getId();
        this.qualityId = other.getQualityId();
        this.qualityName = qualityName;
        this.qualityType = other.getQualityType();
        this.unit = other.getUnit();
        this.partyId = other.getParty().getId();
        this.wtPer100m = other.getWtPer100m();
        this.remark = other.getRemark();
        this.createdDate = other.getCreatedDate();
        this.createdBy = other.getUserCreatedByData().getId();
        this.updatedBy = other.getUpdatedBy().getId();
        this.updatedDate = other.getUpdatedDate();
//		this.qualityDate = other.qualityDate;
        this.rate= other.getRate();
        this.userHeadId = other.getUserHeadData().getId();
        this.qualityNameId=other.getQualityName().getId();
        this.billingUnit=other.getBillingUnit();
        this.mtrPerKg=other.getMtrPerKg();
    }



}
