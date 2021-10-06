package com.main.glory.model.shade.requestmodals;

import com.main.glory.model.shade.ShadeData;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.user.UserData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddShadeMast {

    String partyShadeNo;
    Long id;
    Long processId;
    String processName;
    String colorTone;
    String qualityId;
    String qualityName;
    String qualityType;
    Long partyId;
    Long cuttingId;
    String labColorNo;
    String category;
    String remark;
    String apcNo;
    Long qualityEntryId;
    Boolean pending;
    Double extraRate;
    String colorName;
    List<ShadeData> shadeDataList;

    private Long createdBy;
    private Long updatedBy;
    private Long userHeadId;


    public AddShadeMast(ShadeMast shadeMast) {
        this.partyShadeNo = shadeMast.getPartyShadeNo();
        this.id = shadeMast.getId();
        this.processId = shadeMast.getProcessId();
        this.processName = shadeMast.getProcessName();
        this.colorTone = shadeMast.getColorTone();
        this.qualityId = shadeMast.getQuality().getQualityId();
        this.qualityName = shadeMast.getQuality().getQualityName().getQualityName();
        this.qualityType = shadeMast.getQuality().getQualityType();
        this.partyId = shadeMast.getParty().getId();
        this.cuttingId = shadeMast.getCuttingId();
        this.labColorNo = shadeMast.getLabColorNo();
        this.category = shadeMast.getCategory();
        this.remark = shadeMast.getRemark();
        this.apcNo = shadeMast.getApcNo();
        this.qualityEntryId = shadeMast.getQuality().getId();
        this.pending = shadeMast.getPending();
        this.extraRate = shadeMast.getExtraRate();
        this.colorName = shadeMast.getColorName();
        this.shadeDataList = shadeMast.getShadeDataList();
        this.createdBy = shadeMast.getCreatedBy().getId();
        this.updatedBy = shadeMast.getUpdatedBy()==null?null:shadeMast.getUpdatedBy().getId();
        this.userHeadId = shadeMast.getUserHeadData()==null?null:shadeMast.getUserHeadData().getId();
    }

}
