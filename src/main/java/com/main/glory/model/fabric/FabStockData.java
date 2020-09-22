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
