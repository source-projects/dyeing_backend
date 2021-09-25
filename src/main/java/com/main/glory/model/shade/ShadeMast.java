package com.main.glory.model.shade;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.StockDataBatchData.request.BatchDetail;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.shade.requestmodals.AddShadeMast;
import com.main.glory.model.user.UserData;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ShadeMast")
@ToString
	public class ShadeMast {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	String partyShadeNo;
	Long processId;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="qualityEntryId", referencedColumnName = "id", insertable = true, updatable = true)    
	Quality quality;
	Long partyId;
	String colorTone;

    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="createdBy", referencedColumnName = "id", insertable = true, updatable = true)    
    private UserData createdBy;
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="updatedById", referencedColumnName = "id", insertable = true, updatable = true)    
    private UserData updatedBy;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="userHeadId", referencedColumnName = "id", insertable = true, updatable = true)
    private UserData userHeadData;

	@ApiModelProperty(hidden = true)
	Date createdDate;
	@ApiModelProperty(hidden = true)
	Date updatedDate;
	Long cuttingId;
	String remark;
	String category;
	String labColorNo;
	String processName;
	String apcNo;
	Boolean pending;
	Double extraRate;
	@Column(columnDefinition = "varchar(255) default 'Not mentioned'")
	String colorName;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "controlId", referencedColumnName = "id")
	List<ShadeData> shadeDataList;


	@PrePersist
	protected void onCreate(){this.createdDate=new Date(System.currentTimeMillis());}
	@PreUpdate
	protected void onUpdate(){ this.updatedDate = new Date(System.currentTimeMillis()); }

	public ShadeMast(AddShadeMast addShadeMast,UserData createdBy,UserData userHeadData,Quality quality)
	{
		this.apcNo =addShadeMast.getApcNo();
		this.pending=addShadeMast.getPending();
		this.id=addShadeMast.getPartyId();
		this.quality=quality;
		this.partyShadeNo=addShadeMast.getPartyShadeNo();
		this.processId=addShadeMast.getProcessId();
		this.partyId=addShadeMast.getPartyId();
		this.colorTone=addShadeMast.getColorTone();
		this.createdBy=createdBy;
		this.cuttingId=addShadeMast.getCuttingId();
		this.remark=addShadeMast.getRemark();
		this.category=addShadeMast.getCategory();
		this.labColorNo=addShadeMast.getLabColorNo();
		this.processName=addShadeMast.getProcessName();
		this.userHeadData=userHeadData;
		this.extraRate = addShadeMast.getExtraRate();
		this.colorName=addShadeMast.getColorName();
		//this.shadeDataList=addShadeMast.getShadeDataList();
	}

	public ShadeMast(ShadeMast addShadeMast)
	{
		this.apcNo =addShadeMast.getApcNo();
		this.pending=addShadeMast.getPending();
		this.id=addShadeMast.getId();
		this.partyShadeNo=addShadeMast.getPartyShadeNo();
		this.processId=addShadeMast.getProcessId();
		this.partyId=addShadeMast.getPartyId();
		this.colorTone=addShadeMast.getColorTone();
		this.createdBy=addShadeMast.getCreatedBy();
		this.cuttingId=addShadeMast.getCuttingId();
		this.remark=addShadeMast.getRemark();
		this.category=addShadeMast.getCategory();
		this.labColorNo=addShadeMast.getLabColorNo();
		this.processName=addShadeMast.getProcessName();
		this.userHeadData=addShadeMast.getUserHeadData();
		this.extraRate=addShadeMast.getExtraRate();
		//this.shadeDataList=addShadeMast.getShadeDataList();
	}

	//for get shade by id
	public ShadeMast(ShadeMast addShadeMast,String processName)
	{
		this.id=addShadeMast.getId();
		this.partyShadeNo=addShadeMast.getPartyShadeNo();
		this.processId=addShadeMast.getProcessId();
		this.quality=addShadeMast.getQuality();
		this.partyId=addShadeMast.getPartyId();
		this.colorTone=addShadeMast.getColorTone();
		this.createdBy = addShadeMast.getCreatedBy();
		this.updatedBy = addShadeMast.getUpdatedBy();
		this.createdDate = addShadeMast.getCreatedDate();
		this.updatedDate = addShadeMast.getUpdatedDate();
		this.userHeadData = addShadeMast.getUserHeadData();
		this.cuttingId=addShadeMast.getCuttingId();
		this.remark=addShadeMast.getRemark();
		this.category=addShadeMast.getCategory();
		this.labColorNo=addShadeMast.getLabColorNo();
		this.processName=processName;
		this.apcNo =addShadeMast.getApcNo();
		this.pending=addShadeMast.getPending();
		this.extraRate=addShadeMast.getExtraRate();
		this.colorName = addShadeMast.getColorName();
		this.shadeDataList = addShadeMast.getShadeDataList();

	}
	public ShadeMast(Long id2, String partyShadeNo2, Long processId2, Long qualityEntryId2, Long partyId2,
			String colorTone2, UserData createdBy2, UserData updatedBy2, Date createdDate2, Date updatedDate2,
			UserData userHeadData2, Long cuttingId2, String remark2, String category2, String labColorNo2,
			String processName2, String apcNo2, Boolean pending2, Double extraRate2, String colorName2,
			List<ShadeData> shadeDataList2) {
	}
}
