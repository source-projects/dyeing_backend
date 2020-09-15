package com.main.glory.model.shade.responsemodals;

import com.main.glory.model.shade.ShadeData;
import com.main.glory.model.shade.ShadeMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShadeMastWithDetails extends ShadeMast {

	String partyName;
	String qualityName;
	String qualityType;
//	String processName;

	public ShadeMastWithDetails(Long id, String partyShadeNo, Long processId, Long qualityId, Long partyId, String colorTone, String createdBy, String updatedBy, Date createdDate, Date updatedDate, Boolean isActive, Long userHeadId, Long cuttingId, String remark, String category, Long labColorNo, List<ShadeData> shadeDataList) {
		super(id, partyShadeNo, processId, qualityId, partyId, colorTone, createdBy, updatedBy, createdDate, updatedDate, isActive, userHeadId, cuttingId, remark, category, labColorNo, shadeDataList);
	}

	public ShadeMastWithDetails(ShadeMast shadeMast) {
		super(shadeMast.getId(), shadeMast.getPartyShadeNo(), shadeMast.getProcessId(), shadeMast.getQualityId(), shadeMast.getPartyId(), shadeMast.getColorTone(), shadeMast.getCreatedBy(), shadeMast.getUpdatedBy(), shadeMast.getCreatedDate(), shadeMast.getUpdatedDate(), shadeMast.getIsActive(), shadeMast.getUserHeadId(), shadeMast.getCuttingId(), shadeMast.getRemark(), shadeMast.getCategory(), shadeMast.getLabColorNo(), shadeMast.getShadeDataList());

	}
}
