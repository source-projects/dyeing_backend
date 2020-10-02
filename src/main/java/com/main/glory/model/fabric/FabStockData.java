package com.main.glory.model.fabric;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FabStockData {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long controlId;
	private Long gr;
	private Long qualityId;
	private String qualityName;
	private String qualityType;
	private Double mtr;
	private Double wt;
	@ApiModelProperty(hidden = true)
	private Date createdDate;
	@ApiModelProperty(hidden = true)
	private Date updatedDate;
	private String createdBy;
	private String updatedBy;
	private Boolean isCut;
	private Boolean batchCreated;

	public FabStockData(FabStockData other) {
		this.id = other.id;
		this.controlId = other.controlId;
		this.gr = other.gr;
		this.qualityId = other.qualityId;
		this.qualityName = other.qualityName;
		this.qualityType = other.qualityType;
		this.mtr = other.mtr;
		this.wt = other.wt;
		this.createdDate = other.createdDate;
		this.updatedDate = other.updatedDate;
		this.createdBy = other.createdBy;
		this.updatedBy = other.updatedBy;
		this.isCut = other.isCut;
		this.batchCreated = other.batchCreated;
	}

	@PrePersist
	protected void onCreate() {
		this.createdDate = new Date(System.currentTimeMillis());
		this.isCut = false;
		this.batchCreated = false;
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedDate = new Date(System.currentTimeMillis());
	}
}
