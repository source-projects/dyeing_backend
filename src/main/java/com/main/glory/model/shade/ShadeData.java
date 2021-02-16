package com.main.glory.model.shade;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ShadeData")
public class ShadeData {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(hidden = true)
	Long id;
	Long controlId;
	String itemName;
	Long supplierId;
	String supplierName;
	Double rate;
	Double amount;
	Double concentration;
	@ApiModelProperty(hidden = true)
	Date createdDate;
	@ApiModelProperty(hidden = true)
	Date updatedDate;
	@ApiModelProperty(hidden = true)
	Long createdBy;
	@ApiModelProperty(hidden = true)
	Long updatedBy;
	@Column(nullable = false)
	Long supplierItemId;

}
