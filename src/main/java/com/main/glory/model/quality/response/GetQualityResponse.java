package com.main.glory.model.quality.response;

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
    private String qualityType;
    private String unit;
    private Long  partyId;
    private Double wtPer100m;
    private String remark;
    private  Date createdDate;
    private String createdBy;
    private String updatedBy;
    private Date updatedDate;
    private Date qualityDate;
    private Long userHeadId;
    private String  partyName;
}
