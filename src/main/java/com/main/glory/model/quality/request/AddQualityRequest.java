package com.main.glory.model.quality.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddQualityRequest {
    Long id;
    private String qualityId;
    private Long qualityNameId;
    private String qualityName;
    private String qualityType;
    private String unit;
    private Long partyId;
    private Double wtPer100m;
    private String remark;
    private Long createdBy;
    private  Long updatedBy;
    private Long userHeadId;
    private Double rate;
    Double mtrPerKg;
    String billingUnit;
}
