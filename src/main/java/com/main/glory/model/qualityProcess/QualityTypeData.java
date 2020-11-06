package com.main.glory.model.qualityProcess;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Entity
@Table
public class QualityTypeData {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(hidden = true)
	Long id;
	Long controlId;
	String colorName;
	Double concentration;
	String unit;
	String SupplierName;
	@ApiModelProperty(hidden = true)
	Date createdDate;
	@ApiModelProperty(hidden = true)
	Date UpdatedDate;
	String createdBy;
	String updatedBy;

	public QualityTypeData() {
		this.createdDate = new Date(System.currentTimeMillis());
	}
}
