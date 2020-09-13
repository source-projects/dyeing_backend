package com.main.glory.model.shade;

import io.swagger.annotations.ApiModelProperty;
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
@Entity
@Table(name = "ShadeMast")
public class ShadeMast {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(hidden = true)
	Long id;
	String partyShadeNo;
	Long processId;
	Long qualityId;
	String qualityName;
	String qualityType;
	String partyName;
	String colorTone;
	String createdBy;
	String updatedBy;
	@ApiModelProperty(hidden = true)
	Date createdDate;
	@ApiModelProperty(hidden = true)
	Date updatedDate;
	@ApiModelProperty(hidden = true)
	Boolean isActive;
	Long userHeadId;
	Long cuttingId;
	String remark;
	String category;
	Long labColorNo;
	@Transient
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "controlId", referencedColumnName = "id")
	List<ShadeData> shadeDataList;
}
