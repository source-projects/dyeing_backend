package com.main.glory.model.shade.requestmodals;

import com.main.glory.model.shade.ShadeData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddShadeMast {

    String partyShadeNo;
    Long createdBy;
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
    Long userHeadId;
    List<ShadeData> shadeDataList;


}
