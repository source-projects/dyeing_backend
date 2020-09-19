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
public class QualityProcessMast {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(hidden = true)
	Long id;
	String name;
	Long noOfDyeingBath;
	Double DCMultiplyingFactor;
	@ApiModelProperty(hidden = true)
	Date createdDate;
	@ApiModelProperty(hidden = true)
	Date updatedDate;
	String createdBy;
	String updatedBy;

	@Transient
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(referencedColumnName = "id", name = "controlId")
	List<QualityProcessData> qualityProcessData;

	public QualityProcessMast() {
		this.createdDate = new Date(System.currentTimeMillis());
	}
}
