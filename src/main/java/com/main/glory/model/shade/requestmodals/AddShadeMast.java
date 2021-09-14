package com.main.glory.model.shade.requestmodals;

import com.main.glory.model.shade.ShadeData;
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



}
