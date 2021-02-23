package com.main.glory.model.shade;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.shade.requestmodals.AddShadeMast;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
	//@Column(unique = true,nullable = false)
	String partyShadeNo;
	//@Column(nullable = false)
	Long processId;

	//@Column(nullable = false)

	Long qualityEntryId;

	//@Column(nullable = false)

	Long partyId;
	String colorTone;
	Long createdBy;
	Long updatedBy;
	@ApiModelProperty(hidden = true)
	Date createdDate;
	@ApiModelProperty(hidden = true)
	Date updatedDate;;
	//@Column(nullable = false)
	Long userHeadId;
	//@Column(nullable = false)
	Long cuttingId;
	String remark;
	//@Column(nullable = false)
	String category;
	//@Column(nullable = false)
	String labColorNo;
	String processName;
	String apcNo;
	Boolean pending;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "controlId", referencedColumnName = "id")
	List<ShadeData> shadeDataList;


	@PrePersist
	protected void onCreate(){this.createdDate=new Date(System.currentTimeMillis());}
	@PreUpdate
	protected void onUpdate(){ this.updatedDate = new Date(System.currentTimeMillis()); }

	public ShadeMast(AddShadeMast addShadeMast)
	{
		this.apcNo =addShadeMast.getApcNo();
		this.pending=addShadeMast.getPending();
		this.id=addShadeMast.getPartyId();
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
		this.userHeadId=addShadeMast.getUserHeadId();
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
		this.userHeadId=addShadeMast.getUserHeadId();
		//this.shadeDataList=addShadeMast.getShadeDataList();
	}

}
