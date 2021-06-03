package com.main.glory.model.quality;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.StockDataBatchData.StockMast;

import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.program.Program;
import com.main.glory.model.quality.request.AddQualityRequest;
import com.main.glory.model.shade.ShadeData;
import com.main.glory.model.shade.ShadeMast;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="quality")
@ToString
public class Quality {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
	Long updatedBy;
	Date updatedDate;
	Date qualityDate;
	Long userHeadId;
	Double rate;
	@ColumnDefault("998821")
	String HSN;
	Long qualityNameId;
	@ColumnDefault("meter")
	String billingUnit;

	public Quality(Quality other) {
		this.id = other.id;
		this.qualityId = other.qualityId;
		//this.qualityName = other.qualityName;
		this.qualityType = other.qualityType;
		this.unit = other.unit;
		this.partyId = other.partyId;
		this.wtPer100m = other.wtPer100m;
		this.remark = other.remark;
		this.createdDate = other.createdDate;
		this.createdBy = other.createdBy;
		this.updatedBy = other.updatedBy;
		this.updatedDate = other.updatedDate;
//		this.qualityDate = other.qualityDate;
		this.rate= other.rate;
		this.userHeadId = other.userHeadId;
		this.qualityNameId=other.qualityNameId;
		this.billingUnit=other.getBillingUnit();
		this.mtrPerKg=other.getMtrPerKg();
	}

	//for adding the quality
	public Quality(AddQualityRequest qualityDto) {
		this.qualityId=qualityDto.getQualityId();
		this.qualityName=qualityDto.getQualityName();
		this.qualityNameId=qualityDto.getQualityNameId();
		this.qualityType=qualityDto.getQualityType();
		this.unit=qualityDto.getUnit();
		this.partyId=qualityDto.getPartyId();
		this.wtPer100m=qualityDto.getWtPer100m();
		this.remark=qualityDto.getRemark();
		this.createdBy=qualityDto.getCreatedBy();
		this.updatedBy=qualityDto.getUpdatedBy();
		this.userHeadId=qualityDto.getUserHeadId();
		this.rate=qualityDto.getRate();
		this.billingUnit=qualityDto.getBillingUnit();
		this.mtrPerKg=qualityDto.getMtrPerKg();

	}

	@PrePersist
	protected void onCreate() {
		this.createdDate = new Date(System.currentTimeMillis());
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedDate = new Date(System.currentTimeMillis());
	}
}
