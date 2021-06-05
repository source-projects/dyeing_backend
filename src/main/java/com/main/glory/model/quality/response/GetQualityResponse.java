package com.main.glory.model.quality.response;

import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityWithPartyName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetQualityResponse {
    private Long id;
    private String  qualityId;
    private String  qualityName;
    Long qualityNameId;
    private String qualityType;
    private String unit;
    private Long  partyId;
    private Double rate;
    private Double wtPer100m;
    private String remark;
    private  Date createdDate;
    private Long createdBy;
    private Long updatedBy;
    private Date updatedDate;
    private Date qualityDate;
    private Long userHeadId;
    private String  partyName;
    String billingUnit;
    Double mtrPerKg;
    String partyCode;
    String hsn;//in case sensitive
    Long processId;
    String procecssName;

    public GetQualityResponse(QualityWithPartyName data) {
        this.id= data.getId();
        this.qualityId = data.getQualityId();
        this.qualityName =data.getQualityName();
        this.qualityNameId = data.getQualityNameId();
        this.qualityType = data.getQualityType();
        this.unit=data.getUnit();
        this.partyId = data.getPartyId();
        this.rate = data.getRate();
        this.wtPer100m= data.getWtPer100m();
        this.remark= data.getRemark();
        this.createdBy=data.getCreatedBy();
        this.createdDate=data.getCreatedDate();
        this.updatedBy=data.getUpdatedBy();
        this.updatedDate=data.getUpdatedDate();
        this.qualityDate = data.getQualityDate();
        this.userHeadId=data.getUserHeadId();
        this.partyName=data.getPartyName();
        this.mtrPerKg=data.getMtrPerKg();
        this.partyCode=data.getPartyCode();
        this.hsn = data.getHsn();
    }

    public GetQualityResponse(Quality data) {
        this.id= data.getId();
        this.qualityId = data.getQualityId();
        this.qualityName =data.getQualityName();
        this.qualityNameId = data.getQualityNameId();
        this.qualityType = data.getQualityType();
        this.unit=data.getUnit();
        this.partyId = data.getPartyId();
        this.rate = data.getRate();
        this.wtPer100m= data.getWtPer100m();
        this.remark= data.getRemark();
        this.createdBy=data.getCreatedBy();
        this.createdDate=data.getCreatedDate();
        this.updatedBy=data.getUpdatedBy();
        this.updatedDate=data.getUpdatedDate();
        this.qualityDate = data.getQualityDate();
        this.userHeadId=data.getUserHeadId();
        this.billingUnit=data.getBillingUnit();
        this.mtrPerKg=data.getMtrPerKg();
        this.processId = data.getProcessId();
        this.hsn = data.getHsn();
        //this.partyName=data.getPartyName();

    }
}
