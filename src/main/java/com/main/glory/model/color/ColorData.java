package com.main.glory.model.color;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "color_data")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ColorData {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	Long id;
	@Column(nullable = false)
	Long purchaseId;

	@Column(nullable = false)
	Long itemId;

	@Column(nullable = false)
	Integer quantity;

	@Column(nullable = false)
	Double rate;

	@Column(nullable = false)
	String quantityUnit;

	@Column(nullable = false)
	Integer quantityPerBox;

	@Column(nullable = false)
	Integer noOfBox;

	@Transient
	@ApiModelProperty(hidden = true)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "controlId", referencedColumnName = "id")
	List<ColorBox> colorBoxes;
}
