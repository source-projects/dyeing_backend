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
import java.util.Objects;

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
	String qualityType;
	String unit;// inward units

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    CascadeType.REFRESH })
	@JoinColumn(name="partyId", referencedColumnName = "id", insertable = true, updatable = true)    
	Party  party;

	Double wtPer100m;
	Double mtrPerKg;
	String remark;
	Date createdDate;
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    CascadeType.REFRESH })
	@JoinColumn(name="createdBy", referencedColumnName = "id", insertable = true, updatable = true)    
	UserData userCreatedByData;
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    CascadeType.REFRESH })
	@JoinColumn(name="userHeadId", referencedColumnName = "id", insertable = true, updatable = true)    
	UserData userHeadData;
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    CascadeType.REFRESH })
	@JoinColumn(name="updatedBy", referencedColumnName = "id", insertable = true, updatable = true)    
	UserData updatedBy;
	Date updatedDate;
	Date qualityDate;


	Double rate;
	@Column(columnDefinition = "varchar(255) default '998821'")
	String hsn;
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    CascadeType.REFRESH })
	@JoinColumn(name="qualityNameId", referencedColumnName = "id", insertable = true, updatable = true)    
	QualityName qualityName;

	@Column(columnDefinition = "varchar(255) default 'meter'")
	String billingUnit;
	Long processId;

	public Quality(Quality other) {
		this.id = other.id;
		this.qualityId = other.qualityId;
		this.qualityName = other.qualityName;
		this.qualityType = other.qualityType;
		this.unit = other.unit;
		this.party = other.party;
		this.wtPer100m = other.wtPer100m;
		this.remark = other.remark;
		this.createdDate = new Date(System.currentTimeMillis());
		this.userCreatedByData = other.userCreatedByData;
		this.updatedBy = other.updatedBy;
		this.updatedDate = new Date(System.currentTimeMillis());
		this.qualityDate = other.qualityDate;
		this.rate= other.rate;
		this.userHeadData=other.userHeadData;
		this.qualityName=other.qualityName;
		this.billingUnit=other.getBillingUnit();
		this.mtrPerKg=other.getMtrPerKg();
		this.processId = other.getProcessId()==null?null:other.getProcessId();
		this.hsn = other.getHsn();

	}

	//for adding the quality
	// public Quality(AddQualityRequest qualityDto) {
	// 	this.qualityId=qualityDto.getQualityId();
	// 	this.qualityName=qualityDto.getQualityName();
	// 	this.qualityNameId=qualityDto.getQualityNameId();
	// 	this.qualityType=qualityDto.getQualityType();
	// 	this.unit=qualityDto.getUnit();
	// 	this.party=qualityDto.getParty();
	// 	this.wtPer100m=qualityDto.getWtPer100m();
	// 	this.remark=qualityDto.getRemark();
	// 	this.userCreatedByData=qualityDto.getUserCreatedByData();
	// 	this.updatedBy=qualityDto.getUpdatedBy();
	// 	this.userHeadData=qualityDto.getUserHeadData();
	// 	this.rate=qualityDto.getRate();
	// 	this.billingUnit=qualityDto.getBillingUnit();
	// 	this.mtrPerKg=qualityDto.getMtrPerKg();

	// }

	@PrePersist
	protected void onCreate() {
		this.createdDate = new Date(System.currentTimeMillis());
		this.updatedDate = new Date(System.currentTimeMillis());
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedDate = new Date(System.currentTimeMillis());
	}

	public Quality(AddQuality other, UserData userHeadData, UserData createdBy, UserData updatedBy,
			Party party, QualityName qualityName) {

				this.id = other.id;
				this.qualityId = other.qualityId;
				this.qualityName = qualityName;
				this.qualityType = other.qualityType;
				this.unit = other.unit;
				this.party =party;
				this.wtPer100m = other.wtPer100m;
				this.remark = other.remark;
				this.createdDate = other.createdDate;
				this.userCreatedByData = createdBy;
				this.updatedBy = updatedBy;
				this.updatedDate = other.updatedDate;
				this.qualityDate = other.qualityDate;
				this.rate= other.rate;
				this.userHeadData=userHeadData;
				this.billingUnit=other.getBillingUnit();
				this.mtrPerKg=other.getMtrPerKg();
				this.processId = other.getProcessId()==null?null:other.getProcessId();
				this.hsn = other.getHsn();
		
		
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Quality quality = (Quality) o;
		return id.equals(quality.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
