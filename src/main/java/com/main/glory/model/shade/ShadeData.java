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
	Long id;
	@Column(nullable = true)
	Long controlId;
	@Column(nullable = true)
	String itemName;
	@Column(nullable = true)
	Long supplierId;
	@Column(nullable = true)
	String supplierName;
	@Column(nullable = true)
	Double rate;
	@Column(nullable = true)
	Double amount;
	@Column(nullable = true)
	Double concentration;
	@ApiModelProperty(hidden = true)
	Date createdDate;
	@ApiModelProperty(hidden = true)
	Date updatedDate;
	@ApiModelProperty(hidden = true)
	Long createdBy;
	@ApiModelProperty(hidden = true)
	Long updatedBy;
	@Column(nullable = true)
	Long supplierItemId;

}
