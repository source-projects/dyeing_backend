package com.main.glory.model.quality;

import lombok.*;

import java.util.Date;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class AddQuality {
	Long id;
	String  qualityId;
	String qualityName;
	String qualityType;
	String unit;
	Long  partyId;
	Double wtPer100m;
	Double mtrPerKg;
	String remark;
	Date createdDate;
	Long createdBy;
	Long userHeadId;	
	Long updatedBy;
	Date updatedDate;
	Date qualityDate;
	Double rate;
	String hsn;
	Long qualityNameId;
	String billingUnit;
	Long processId;

}
