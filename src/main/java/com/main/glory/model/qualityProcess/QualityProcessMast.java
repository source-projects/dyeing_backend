package com.main.glory.model.qualityProcess;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Entity
@Table
@NoArgsConstructor
public class QualityProcessMast {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(hidden = true)
	Long id;
	String name;
	Long noOfDyeingBath;
	Double DCMultiplyingFactor;
	@ApiModelProperty(hidden = true)
	Date createdDate;
	@ApiModelProperty(hidden = true)
	Date updatedDate;
	Long createdBy;
	Long updatedBy;
	Long userHeadId;
	@Transient
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(referencedColumnName = "id", name = "controlId")
	List<QualityProcessData> qualityProcessData;

	@PrePersist
	protected void onCreate() {
		this.createdDate = new Date(System.currentTimeMillis());
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedDate = new Date(System.currentTimeMillis());
	}
}
