package com.main.glory.model.shade;

import com.main.glory.model.quality.Quality;
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

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "quality_entry_Id", referencedColumnName = "id")
	Quality qualityId;
	//@Column(nullable = false)
	Long partyId;
	String colorTone;
	String createdBy;
	String updatedBy;
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

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "controlId", referencedColumnName = "id")
	List<ShadeData> shadeDataList;

	@PreUpdate
	protected void onUpdate(){ this.updatedDate = new Date(System.currentTimeMillis()); }
}
