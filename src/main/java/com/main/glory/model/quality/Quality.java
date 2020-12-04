package com.main.glory.model.quality;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.jobcard.JobMast;
import com.main.glory.model.program.Program;
import com.main.glory.model.shade.ShadeData;
import com.main.glory.model.shade.ShadeMast;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
    private Long id;
	private String  qualityId;
	private String  qualityName;
	private String qualityType;
	private String unit;
	private Long  partyId;
	private Double wtPer100m;
	private String remark;
	private  Date createdDate;
	private Long createdBy;
	private Long updatedBy;
	private Date updatedDate;
	private Date qualityDate;
	private Long userHeadId;

	@ApiModelProperty(hidden = true)
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "qualityId", referencedColumnName = "id")
	List<StockMast> stockMasts;

	@ApiModelProperty(hidden = true)
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "qualityEntryId", referencedColumnName = "id")
	private List<Program> program;

	@ApiModelProperty(hidden = true)
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "qualityEntryId", referencedColumnName = "id")
	private List<ShadeMast> shadeMasts;

	@ApiModelProperty(hidden = true)
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "qualityEntryId", referencedColumnName = "id")
	private List<JobMast> jobMast;

	public Quality(Quality other) {
		this.id = other.id;
		this.qualityId = other.qualityId;
		this.qualityName = other.qualityName;
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
		this.userHeadId = other.userHeadId;
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
