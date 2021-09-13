package com.main.glory.model.quality;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.party.Party;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.program.Program;
import com.main.glory.model.quality.request.AddQualityRequest;
import com.main.glory.model.shade.ShadeData;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.user.UserData;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class AddQuality {
	Long id;
	String  qualityId;
	String qualityName;
	String qualityType;
	String unit;// inward units
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
