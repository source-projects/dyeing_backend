package com.main.glory.model.shade;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ShadeMast")
@ToString
public class ShadeMast {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(hidden = true)
	Long id;
	@Column(unique = true,nullable = false)
	String partyShadeNo;
	@Column(nullable = false)
	Long processId;
	@Column(nullable = false)
	Long qualityId;
	@Column(nullable = false)
	Long partyId;
	String colorTone;
	String createdBy;
	String updatedBy;
	@ApiModelProperty(hidden = true)
	Date createdDate;
	@ApiModelProperty(hidden = true)
	Date updatedDate;
	@ApiModelProperty(hidden = true)
	Boolean isActive;
	@Column(nullable = false)
	Long userHeadId;
	@Column(nullable = false)
	Long cuttingId;
	String remark;
	@Column(nullable = false)
	String category;
	@Column(nullable = false)
	Long labColorNo;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "controlId", referencedColumnName = "id")
	List<ShadeData> shadeDataList;
}
